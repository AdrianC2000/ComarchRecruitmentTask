package database;

import data.Parsers;
import data.messages.ReturnMessage;
import data.Validators;

import java.sql.*;

public class DatabaseHandler {
    private static Connection connection = null;

    public static Connection getConnection() {
        return connection;
    }

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

    public static ReturnMessage deleteResource(String tableName, Integer id) {
            if (Validators.resourceExistence(tableName, id, connection)) {
                try {
                    String IDname = "ID_" + tableName.substring(0, tableName.length() - 1);
                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate("DELETE FROM " + tableName + " WHERE " + IDname + " = " + id);

                    PreparedStatement preparedStmt = connection.prepareStatement("DELETE FROM " + tableName + " WHERE " + IDname + " = ?");
                    preparedStmt.setInt(1, id);
                    preparedStmt.execute();

                    System.out.println("querying DELETE FROM " + tableName + " WHERE " + IDname + " = " + id);
                    return new ReturnMessage("User with the id " + id + " deleted correctly.",true);
                } catch (SQLException e) {
                    return new ReturnMessage("Error: " + e.getMessage(),false);
                }
            }
            return new ReturnMessage(Parsers.resourceName(tableName) +
                    " with the id " + id + " does not exist.", false);
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