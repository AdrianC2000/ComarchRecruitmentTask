package Database;

import Data.Parsers;
import Data.ReturnMessage;
import Data.Validators;
import Models.User;
import Models.UserRequirements;
import com.google.gson.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseHandlerUser {

    public static ReturnMessage addResourceUser(String tableName, Object object, Connection connection) {
        Field[] fields = User.class.getDeclaredFields();
        ArrayList<String> fieldsList = Parsers.parseFieldsArrayIntoStringList(fields);
        boolean areFieldsValid = Validators.fieldsValidation(object, fieldsList, "add");
        if(areFieldsValid) {
            PreparedStatement preparedStmt = null;
            try {

                Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
                String tmp = gson.toJson(object);

                User newUser = gson.fromJson(tmp, User.class);

                String query = "INSERT INTO " + tableName + " (login, email, first_name, last_name, creation_date) VALUES (?, ?, ?, ?, ?)";
                preparedStmt = connection.prepareStatement(query);

                for (int i = 1; i < fields.length; i++) {
                    // invoking the getter
                    String methodName = "get" + fields[i].getName().substring(0, 1).toUpperCase() + fields[i].getName().substring(1);
                    Method method = User.class.getDeclaredMethod(methodName);
                    String var = (String) method.invoke(newUser);
                    preparedStmt.setString(i, var);
                }

                assert preparedStmt != null;
                preparedStmt.execute();

                System.out.println("querying INSERT INTO " + tableName);
                return new ReturnMessage("Resource added correctly.", null, true);
            } catch (SQLException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
                return new ReturnMessage("Database error: " + e.getMessage(), null, false);
            }
        }
        return new ReturnMessage("Fields error: One or more fields names are invalid.", null, false);
    }

    public static ReturnMessage filterUser (String tableName, Object object, String logic) {
        Field[] fields = UserRequirements.class.getDeclaredFields();
        ArrayList<String> fieldsList = Parsers.parseFieldsArrayIntoStringList(fields);
        boolean areFieldsValid = Validators.fieldsValidation(object, fieldsList, "filter");
        if (areFieldsValid) {
            try {
                // Getting all the records
                List<Object> allRecordsObject = DatabaseHandler.getResource(tableName).getResult();
                List<User> allRecordsUser = Parsers.parseListObjectIntoListUser(allRecordsObject);

                // Getting all the requirements
                Gson gson = new Gson();
                String tmp = gson.toJson(object);
                UserRequirements allRequirements = gson.fromJson(tmp, UserRequirements.class);

                // Separating signs from variables
                ArrayList<String> variables = new ArrayList<>();
                ArrayList<String> parametersGettersNames = new ArrayList<>();


                for (Field field : fields) {
                    // invoking the getter
                    String methodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                    Method method = UserRequirements.class.getDeclaredMethod(methodName);
                    String[] var = (String[]) method.invoke(allRequirements);
                    System.out.println(methodName + " " + Arrays.toString(var));
                    if (var != null) {
                        for (String j : var) {
                            variables.add(j.toString());
                            parametersGettersNames.add(methodName);
                        }
                    }
                }

                if (logic.equals("AND"))
                    return new ReturnMessage("OK", filterUserAnd(variables, parametersGettersNames, allRecordsUser), true);
                else if (logic.equals("OR"))
                    return new ReturnMessage("OK", filterUserOr(variables, parametersGettersNames, allRecordsUser), true);
                else
                    return new ReturnMessage("Incorrect logic name.", null, false);

            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
                return new ReturnMessage("Error: " + e.getMessage(), null, false);
            }
        }
        return new ReturnMessage("Error: One or more fields names are invalid.", null, false);
    }

    public static List<Object> filterUserAnd (ArrayList<String> variables, ArrayList<String> parametersGettersNames, List<User> allRecordsUser) {

        for (int i = 0; i < variables.size(); i++) {
            String var = variables.get(i);
            String param = parametersGettersNames.get(i);
            System.out.println(var + " " + param);
            allRecordsUser = allRecordsUser.stream()
                    .filter(user -> user.allMethodsGetter(param, user).equals(var))
                    .collect(Collectors.toList());

        }

        allRecordsUser.sort(Comparator.comparing(User::getID_user));

        return Parsers.parseListUserIntoListObject(allRecordsUser);
    }

    public static List<Object> filterUserOr (ArrayList<String> variables, ArrayList<String> parametersGettersNames, List<User> allRecordsUser) {
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

        allRecordsFiltered.sort(Comparator.comparing(User::getID_user));

        return Parsers.parseListUserIntoListObject(allRecordsFiltered);
    }
}
