package Database;

import Data.Book;
import Data.User;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static Connection connection = null;

    public static boolean establishConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/comarch_database";
            String user = "Adi";
            String password = "DatabasePassword123";
            connection = DriverManager.getConnection(url, user, password);
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Object> getResource(String tableName) {
        try {
            System.out.println(tableName);
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM " + tableName);
            System.out.println("querying SELECT * FROM " + tableName);
            return parseIntoObjectObjectList(tableName, result);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean resourceExistence(String table, Integer id) {
        try {
            String IDname = "ID_" + table.substring(0, table.length() - 1);
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("SELECT 1 FROM " + table + " WHERE " + IDname + " = " + id);
            System.out.println("SELECT 1 FROM " + table + " WHERE " + IDname + " = " + id);
            System.out.println("quering SELECT 1 FROM " + table + " WHERE ID_user = " + id);
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean tableExistence(String table) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("SELECT 1 FROM " + table);
            System.out.println("SELECT 1 FROM " + table);
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Integer addResource(String tableName, Object object) {
        PreparedStatement preparedStmt = null;
        if (tableExistence(tableName)) {
            try {
                if (tableName.equals("users")) {
                    Gson gson = new Gson();
                    String tmp = gson.toJson(object);
                    User newUser = gson.fromJson(tmp, User.class);

                    Field[] fields = User.class.getDeclaredFields();

                    String query = "INSERT INTO " + tableName + " (login, email, first_name, last_name, creation_date) VALUES (?, ?, ?, ?, ?)";
                    preparedStmt = connection.prepareStatement(query);

                    for (int i = 1; i < fields.length; i++) {
                        // invoking the getter
                        Method method = User.class.getDeclaredMethod("get" + fields[i].getName().substring(0, 1).toUpperCase() + fields[i].getName().substring(1));
                        String var = (String) method.invoke(newUser);
                        preparedStmt.setString(i, var);
                    }
                } else if (tableName.equals("books")) {
                    Gson gson = new Gson();
                    String tmp = gson.toJson(object);
                    Book newBook = gson.fromJson(tmp, Book.class);

                    Field[] fields = Book.class.getDeclaredFields();

                    String query = "INSERT INTO " + tableName + " (title, author, is_taken, taken_by, taken_date, return_date) VALUES (?, ?, ?, ?, ?, ?)";
                    preparedStmt = connection.prepareStatement(query);

                    for (int i = 1; i < fields.length; i++) {
                        // invoking the getter
                        String methodName = "get" + fields[i].getName().substring(0, 1).toUpperCase() + fields[i].getName().substring(1);
                        Method method = Book.class.getDeclaredMethod(methodName);

                        String var;
                        Boolean isTaken;
                        if (methodName.equals("getIs_taken")) {
                            isTaken = (Boolean) method.invoke(newBook);
                            System.out.println(isTaken);
                            preparedStmt.setBoolean(i, isTaken);
                        } else {
                            var = (String) method.invoke(newBook);
                            preparedStmt.setString(i, var);
                        }
                    }
                }

                assert preparedStmt != null;
                preparedStmt.execute();

                System.out.println("querying INSERT INTO " + tableName);

                return 1;
            } catch (SQLException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
                return 0;
            }
        }
        else
            return 2;
    }

    public static Integer updateResource(String table, Integer id, String parameter, String valueToSet) {
        if (tableExistence(table)) {
            try {
                String IDname = "ID_" + table.substring(0, table.length() - 1);
                if (resourceExistence(table, id)) {
                    Statement stmt = connection.createStatement();
                    String query = "UPDATE " + table + " SET " + parameter + " = '" + valueToSet + "' WHERE " + IDname + " = " + id;
                    System.out.println(query);
                    stmt.executeUpdate("UPDATE " + table + " SET " + parameter + " = '" + valueToSet + "' WHERE " + IDname + " = " + id);
                    System.out.println("querying UPDATE " + table + " WHERE ID_user = " + id);
                    return 1;
                } else
                    return 3;
            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            }
        }
        else
            return 2;
    }

    public static Integer deleteResource(String tableName, Integer id) {
        // 0 - format error
        // 1 - OK
        // 2 - table does not exist
        // 3 - user does not exist
        if (tableExistence(tableName)) {
            if (resourceExistence(tableName, id)) {
                try {
                    String IDname = "ID_" + tableName.substring(0, tableName.length() - 1);
                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate("DELETE FROM " + tableName + " WHERE " + IDname + " = " + id);
                    System.out.println("querying DELETE FROM " + tableName + " WHERE " + IDname + " = " + id);
                    return 1;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
            return 3;
        }
        return 2;
    }

    /*public static Integer deleteResource(String tableName, Object filter) {

    }*/

    public static List<Object> parseIntoObjectObjectList(String tableName, ResultSet result) {
        try {
            ResultSetMetaData rsmd = result.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            List<Object> listOfUsers = new ArrayList<>();

            if (tableName.equals("users")) {
                while (result.next()) {
                    User actualUser = new User();
                    for (int i = 1; i <= columnsNumber; i++) {
                        String columnValue = result.getString(i);
                        String columnName = rsmd.getColumnName(i);
                        // invoking the setter
                        Method method = User.class.getDeclaredMethod("set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1), String.class);
                        method.invoke(actualUser, columnValue);
                    }
                    listOfUsers.add(actualUser);
                }
            } else if (tableName.equals("books")) {
                while (result.next()) {
                    Book actualBook = new Book();
                    for (int i = 1; i <= columnsNumber; i++) {
                        String columnValue = result.getString(i);
                        String columnName = rsmd.getColumnName(i);
                        String methodName = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                        // invoking the setter
                        if (!methodName.equals("setIs_taken")) {
                            Method method = Book.class.getDeclaredMethod(methodName, String.class);
                            method.invoke(actualBook, columnValue);
                        }
                        else {
                            Method method = Book.class.getDeclaredMethod(methodName, Integer.class);
                            method.invoke(actualBook, Integer.parseInt(columnValue));
                        }
                    }
                    listOfUsers.add(actualBook);
                }
            }
            return listOfUsers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String closeConnection() {
        try {
            connection.close();
            return "Connection closed correctly.";
        } catch (NullPointerException | SQLException e) {
            return "An error occurred during closing the connection";
        }
    }
}
