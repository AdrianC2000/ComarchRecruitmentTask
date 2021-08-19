package database;

import data.Parsers;
import data.messages.ReturnMessage;
import data.messages.ReturnMessageUser;
import data.Validators;
import models.User;
import models.UserRequirements;
import org.gradle.internal.impldep.org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.sql.*;
import java.util.*;

public class DatabaseHandlerUser {

    private static final Connection connection = DatabaseHandler.getConnection();

    public static ReturnMessageUser getUsers(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM " + tableName);
        System.out.println("querying SELECT * FROM " + tableName);
        return new ReturnMessageUser("OK", Parsers.parseResultSetIntoUserList(result), true);
    }

    public static ReturnMessage addUser(String tableName, User newUser) {
        boolean areFieldsValid = Validators.fieldsValidationUser(newUser);
        if (areFieldsValid) {
            PreparedStatement preparedStmt;
            try {
                String query = "INSERT INTO " + tableName + " (login, email, first_name, last_name, creation_date) VALUES (?, ?, ?, ?, ?)";
                preparedStmt = connection.prepareStatement(query);
                System.out.println(Parsers.prepareUser(preparedStmt, newUser));
                Parsers.prepareUser(preparedStmt, newUser).execute();
                System.out.println("querying INSERT INTO " + tableName);
                return new ReturnMessage("Resource added correctly.", true);
            } catch (SQLException e) {
                return new ReturnMessage("Database error: " + e.getMessage() + ".\nCheck your input for typos.", false);
            }
        } else {
            return new ReturnMessage("Fields error: Check your input for typos or forgotten parameters.", false);
        }
    }

    public static ReturnMessage updateUser(String tableName, Integer id, String parameter, String valueToSet) {
        if (parameter.contains("ID")) {
            return new ReturnMessage("You can't edit the ID field.", false);
        }
        if (valueToSet.equals("null")) {
            valueToSet = null;
        }
        try {
            String IDname = "ID_" + tableName.substring(0, tableName.length() - 1);
            if (parameter.contains("taken_by") && valueToSet != null) {
                try {
                    if (!Validators.resourceExistence(tableName, Integer.parseInt(valueToSet), connection)) {
                        throw new SQLException("Database error: " + Parsers.resourceName(tableName) + " with the ID " + id + " does not exist.");
                    }
                } catch (NumberFormatException exc) {
                    throw new SQLException("Wrong taken by value format - it has to be an positive integer or null.");
                }
            }
            if (Validators.resourceExistence(tableName, id, connection)) {
                PreparedStatement preparedStmt = connection.prepareStatement("UPDATE " + tableName + " SET " + parameter + " = ? WHERE " + IDname + " = ? ");
                preparedStmt.setString(1, valueToSet);
                preparedStmt.setInt(2, id);
                preparedStmt.execute();

                return new ReturnMessage("Parameter " + parameter + " changed for " + valueToSet + " for " + Parsers.resourceName(tableName).toLowerCase() + " with the id " + id + " correctly.", true);
            } else {
                System.out.println(Validators.resourceExistence(tableName, id, connection));
                return new ReturnMessage(Parsers.resourceName(tableName) + " with the id " + id + " does not exist.", false);
            }
        } catch (SQLException e) {
            return new ReturnMessage("Database error: " + e.getMessage(), false);
        }
    }

    public static ReturnMessageUser filterUser(String tableName, UserRequirements allRequirements, String logic) {
        try {
            System.out.println(ReflectionToStringBuilder.toString(allRequirements));
            HashMap<String, String[]> requirementsMap = Parsers.parseUserRequirementsIntoHashMap(allRequirements);
            StringBuilder query = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");
            for (Map.Entry<String, String[]> entry : requirementsMap.entrySet()) {
                String parameter = entry.getKey();
                String[] values = entry.getValue();
                if (values != null) {
                    System.out.println(parameter);
                    System.out.println(Arrays.toString(values));
                    if (values.length > 1) {
                        for (int i = 0; i < values.length; i++) {
                            if (i == 0) {
                                query.append(parameter).append(" IN (?, ");
                            } else if (i == values.length - 1) {
                                query.append("?)");
                            } else {
                                query.append("?, ");
                            }
                        }
                    } else {
                        query.append(parameter).append(" IN (?) ");
                    }
                    query.append(" ").append(logic).append(" ");
                    System.out.println(query);
                }
            }
            String queryString = "";
            if (logic.equals("AND")) {
                queryString = query.substring(0, query.length() - 5);
            } else if (logic.equals("OR")) {
                queryString = query.substring(0, query.length() - 4);
            }
            PreparedStatement preparedStmt = connection.prepareStatement(queryString);
            int index = 1;
            for (Map.Entry<String, String[]> entry : requirementsMap.entrySet()) {
                String[] values = entry.getValue();
                if (values != null) {
                    for (String value : values) {
                        preparedStmt.setString(index, value);
                        index++;
                    }
                }
                System.out.println(preparedStmt);
            }
            ResultSet result = preparedStmt.executeQuery();

            return new ReturnMessageUser("OK", Parsers.parseResultSetIntoUserList(result), true);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ReturnMessageUser("Database error: " + e.getMessage(), null, false);
        }
    }
}