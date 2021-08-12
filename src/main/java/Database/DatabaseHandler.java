package Database;

import Data.Parsers;
import Data.ReturnMessage;
import Data.Validators;

import java.sql.*;

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

    public static ReturnMessage getResource(String tableName) {
        try {
            System.out.println(tableName);
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM " + tableName);
            System.out.println("querying SELECT * FROM " + tableName);
            return new ReturnMessage(" ", Parsers.parseIntoObjectList(tableName, result), true);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ReturnMessage("Table '" + tableName + "' does not exist.", null, false);
        }
    }

    public static ReturnMessage addResource(String tableName, Object object) {
        System.out.println("Tutaj jestem: addResource");
        if (Validators.tableExistence(tableName, connection)) {
            if (tableName.equals("users")) {
                return DatabaseHandlerUser.addResourceUser(tableName, object, connection);
            } else if (tableName.equals("books")) {
                return DatabaseHandlerBook.addResourceBook(tableName, object, connection);
            }
        }
        return new ReturnMessage("Error: Table with the name " + tableName + " does not exsits.", null, false);
    }

    public static ReturnMessage updateResource(String tableName, Integer id, String parameter, String valueToSet) {
        if (Validators.tableExistence(tableName, connection)) {
            try {
                String IDname = "ID_" + tableName.substring(0, tableName.length() - 1);
                if (Validators.resourceExistence(tableName, id, connection)) {
                    Statement stmt = connection.createStatement();
                    String query = "UPDATE " + tableName + " SET " + parameter + " = '" + valueToSet + "' WHERE " + IDname + " = " + id;
                    System.out.println(query);
                    stmt.executeUpdate("UPDATE " + tableName + " SET " + parameter + " = '" + valueToSet + "' WHERE " + IDname + " = " + id);
                    System.out.println("querying UPDATE " + tableName + " WHERE ID_user = " + id);
                    return new ReturnMessage("Parameter " + parameter + " changed for " + valueToSet + " for " + Parsers.resourceName(tableName).toLowerCase() + " with the id " + id + " correctly.", null, true);
                } else
                    return new ReturnMessage(Parsers.resourceName(tableName) + " with id " + id + " does not exist.", null, false);

            } catch (SQLException e) {
                if (parameter.contains("ID"))
                    return new ReturnMessage("You can't edit the ID field.", null, false);
                e.printStackTrace();
                return new ReturnMessage("Error: Wrong parameter.", null, false);
            }
        } else
            return new ReturnMessage(tableName + " table does not exist.", null, false);
    }

    public static ReturnMessage deleteResource(String tableName, Integer id) {
        if (Validators.tableExistence(tableName, connection)) {
            if (Validators.resourceExistence(tableName, id, connection)) {
                try {
                    String IDname = "ID_" + tableName.substring(0, tableName.length() - 1);
                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate("DELETE FROM " + tableName + " WHERE " + IDname + " = " + id);
                    System.out.println("querying DELETE FROM " + tableName + " WHERE " + IDname + " = " + id);
                    return new ReturnMessage("User with the id " + id + " deleted correctly.", null, true);
                } catch (SQLException e) {
                    return new ReturnMessage("Error: " + e.getMessage(), null, false);
                }
            }
            return new ReturnMessage(Parsers.resourceName(tableName) +
                    " with the id " + id + " does not exist.", null, false);
        }
        return new ReturnMessage("'" + tableName + "' table does not exist.", null, false);
    }

    public static ReturnMessage filterResource(String tableName, Object object, String logic) {
        if (Validators.tableExistence(tableName, connection)) {
            if (tableName.equals("users")) {
                return DatabaseHandlerUser.filterUser(tableName, object, logic);
            } else if (tableName.equals("books"))
                return DatabaseHandlerBook.filterBook(tableName, object, logic);
        }
        return new ReturnMessage("'" + tableName + "' table does not exist.", null, false);
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
