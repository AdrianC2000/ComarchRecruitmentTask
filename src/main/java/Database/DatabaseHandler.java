package Database;

import Data.Book;
import Data.BookRequirements;
import Data.User;
import Data.UserRequirements;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM " + tableName);
            System.out.println("querying SELECT * FROM " + tableName);
            return Parsers.parseIntoObjectList(tableName, result);
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
        } else
            return 2;
    }

    public static Integer updateResource(String table, Integer id, String parameter, String valueToSet) {
        if (tableExistence(table)) {
            try {
                String IDname = "ID_" + table.substring(0, table.length() - 1);
                if (resourceExistence(table, id)) {
                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate("UPDATE " + table + " SET " + parameter + " = '" + valueToSet + "' WHERE " + IDname + " = " + id);
                    System.out.println("querying UPDATE " + table + " WHERE ID_user = " + id);
                    return 1;
                } else
                    return 3;
            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            }
        } else
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

    public static List<Object> filterResource(String tableName, Object object) {
        if (tableExistence(tableName)) {
            try {
                if (tableName.equals("users")) {
                    // Getting all the records
                    List<Object> allRecordsObject = getResource(tableName);
                    List<User> allRecordsUser = Parsers.parseListObjectIntoListUser(allRecordsObject);

                    // Getting all the requirements
                    Gson gson = new Gson();
                    String tmp = gson.toJson(object);
                    UserRequirements allRequirements = gson.fromJson(tmp, UserRequirements.class);

                    // Separating signs from variables
                    ArrayList<String> variables = new ArrayList<>();
                    ArrayList<String> parametersGettersNames = new ArrayList<>();

                    Field[] fields = UserRequirements.class.getDeclaredFields();

                    for (Field field : fields) {
                        // invoking the getter
                        String methodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                        Method method = UserRequirements.class.getDeclaredMethod(methodName);
                        String[] var = (String[]) method.invoke(allRequirements);
                        if (var != null) {
                            for (String j : var) {
                                variables.add(j);
                                parametersGettersNames.add(methodName);
                            }
                        }
                    }

                    List<User> allRecordsFiltered = new ArrayList<>();
                    List<User> tempRecordsFiltered;

                    for (int i = 0; i < variables.size(); i++) {
                        String var = variables.get(i);
                        String param = parametersGettersNames.get(i);

                        tempRecordsFiltered = allRecordsUser.stream()
                                .filter(user -> user.allMethodsGetter(param, user).equals(var))
                                .collect(Collectors.toList());
                        for (User user : tempRecordsFiltered) {
                            if (!allRecordsFiltered.contains(user))
                                allRecordsFiltered.add(user);
                        }
                    }

                    return Parsers.parseListUserIntoListObject(allRecordsFiltered);

                } else if (tableName.equals("books")) {
                    // Getting all the records
                    List<Object> allRecordsObject = getResource(tableName);
                    List<Book> allRecordsUser = Parsers.parseListObjectIntoListBook(allRecordsObject);

                    // Getting all the requirements
                    Gson gson = new Gson();
                    String tmp = gson.toJson(object);
                    BookRequirements allRequirements = gson.fromJson(tmp, BookRequirements.class);

                    // Separating signs from variables
                    ArrayList<String> variables = new ArrayList<>();
                    ArrayList<String> parametersGettersNames = new ArrayList<>();

                    Field[] fields = BookRequirements.class.getDeclaredFields();

                    for (Field field : fields) {
                        // invoking the getter
                        String methodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                        Method method = BookRequirements.class.getDeclaredMethod(methodName);
                        String[] var = (String[]) method.invoke(allRequirements);
                        if (var != null) {
                            for (String j : var) {
                                variables.add(j);
                                parametersGettersNames.add(methodName);
                            }
                        }
                    }

                    List<Book> allRecordsFiltered = new ArrayList<>();
                    List<Book> tempRecordsFiltered;

                    for (int i = 0; i < variables.size(); i++) {
                        String var = variables.get(i);
                        String param = parametersGettersNames.get(i);

                        tempRecordsFiltered = allRecordsUser.stream()
                                .filter(book -> book.allMethodsGetter(param, book).equals(var))
                                .collect(Collectors.toList());
                        for (Book book : tempRecordsFiltered) {
                            if (!allRecordsFiltered.contains(book))
                                allRecordsFiltered.add(book);
                        }
                    }
                    return Parsers.parseListBookIntoListObject(allRecordsFiltered);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
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
