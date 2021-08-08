package Database;

import Data.User;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static Connection connection = null;
    public static String establishConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/comarch_database";
            String user = "Adi";
            String password = "DatabasePassword123";
            connection = DriverManager.getConnection(url, user, password);
            return "Connection established correctly.";
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return "An error occurred during connecting to the database.";
        }
    }

    public static List<User> getResource(String table) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM " + table);
            System.out.println("querying SELECT * FROM " + table);
            return parseIntoObjectUserList(result);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean userExistence(String table, Integer id) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("SELECT 1 FROM " + table + " WHERE ID_user = " + id);
            System.out.println("quering SELECT 1 FROM " + table + " WHERE ID_user = " + id);
            return result.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addResource(String table, User newUser) {
        try {
            Field[] fields = User.class.getDeclaredFields();

            String query = "INSERT INTO " + table + " (login, email, first_name, last_name, creation_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStmt = connection.prepareStatement(query);

            for(int i = 1; i < fields.length; i++) {
                // invoking the getter
                Method method = User.class.getDeclaredMethod("get" + fields[i].getName().substring(0, 1).toUpperCase() + fields[i].getName().substring(1));
                String var = (String) method.invoke(newUser);
                preparedStmt.setString (i, var);
            }

            preparedStmt.execute();

            System.out.println("querying INSERT INTO " + table);
            return true;
        }
        catch (SQLException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateResource(String table, Integer id, String parameter, String valueToSet) {
        try {
            if(userExistence(table, id)) {
                Statement stmt = connection.createStatement();
                String query = "UPDATE " + table + " SET " + parameter + " = '" + valueToSet + "' WHERE ID_user = " + id;
                System.out.println(query);
                stmt.executeUpdate("UPDATE " + table + " SET " + parameter + " = '" + valueToSet + "' WHERE ID_user = " + id);
                System.out.println("querying UPDATE " + table + " WHERE ID_user = " + id);
                return true;
            }
            else
                return false;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteResource(String table, Integer id) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM " + table + " WHERE ID_user = " + id);
            System.out.println("querying DELETE FROM " + table + " WHERE ID_user = " + id);
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<User> parseIntoObjectUserList(ResultSet result) {
        try {
            ResultSetMetaData rsmd = result.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            List<User> listOfUsers = new ArrayList<>();
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
            return listOfUsers;
        }
        catch (Exception e) {
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
