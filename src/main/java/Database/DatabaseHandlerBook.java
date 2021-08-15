package Database;

import Data.Parsers;
import Data.ReturnMessage;
import Data.Validators;
import Models.Book;
import Models.BookRequirements;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vertx.core.json.impl.JsonUtil;
import org.gradle.internal.impldep.org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseHandlerBook {

    public static ReturnMessage addResourceBook(String tableName, Object object, Connection connection) {
        Field[] fields = Book.class.getDeclaredFields();
        ArrayList<String> fieldsList = Parsers.parseFieldsArrayIntoStringList(fields);
        boolean areFieldsValid = Validators.fieldsValidation(object, fieldsList, "add");
        if (areFieldsValid) {
            PreparedStatement preparedStmt = null;
            try {
                Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
                String tmp = gson.toJson(object);
                Book newBook = gson.fromJson(tmp, Book.class);

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
                        try {
                            var = method.invoke(newBook).toString();
                        } catch (NullPointerException e) {
                            var = null;
                        }
                        if (methodName.equals("getTaken_by") && var != null) {
                            if (!Validators.resourceExistence("users", Integer.parseInt(var), connection))
                                throw new SQLException("User with given ID does not exist.");
                        }
                        preparedStmt.setString(i, var);
                    }
                }
                assert preparedStmt != null;
                preparedStmt.execute();

                System.out.println("querying INSERT INTO " + tableName);
                return new ReturnMessage("Resource added correctly.", null, true);
            } catch (SQLException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                return new ReturnMessage("Database error: " + e.getMessage(), null, false);
            }
        }
        return new ReturnMessage("Fields error: One or more fields name are invalid.", null, false);
    }

    public static ReturnMessage filterBook(String tableName, Object object, String logic) {
        Field[] fields = BookRequirements.class.getDeclaredFields();
        ArrayList<String> fieldsList = Parsers.parseFieldsArrayIntoStringList(fields);
        boolean areFieldsValid = Validators.fieldsValidation(object, fieldsList, "filter");
        if (areFieldsValid) {
            try {
                // Getting all the records
                List<Object> allRecordsObject = DatabaseHandler.getResource(tableName).getResult();
                List<Book> allRecordsBook = Parsers.parseListObjectIntoListBook(allRecordsObject);

                // Getting all the requirements
                Gson gson = new Gson();
                String tmp = gson.toJson(object);
                BookRequirements allRequirements = gson.fromJson(tmp, BookRequirements.class);

                // Separating signs from variables
                ArrayList<String> variables = new ArrayList<>();
                ArrayList<String> parametersGettersNames = new ArrayList<>();

                for (Field field : fields) {
                    // invoking the getter
                    String methodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                    Method method = BookRequirements.class.getDeclaredMethod(methodName);
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
                    return new ReturnMessage("OK", filterBookAnd(variables, parametersGettersNames, allRecordsBook), true);
                else if (logic.equals("OR"))
                    return new ReturnMessage("OK", filterBookOr(variables, parametersGettersNames, allRecordsBook), true);
                else
                    return new ReturnMessage("Incorrect logic name.", null, false);

            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                return new ReturnMessage("Error: " + e.getMessage(), null, false);
            }
        }
        return new ReturnMessage("Fields error: One or more fields names are invalid.", null, false);
    }

    public static List<Object> filterBookAnd(ArrayList<String> variables, ArrayList<String> parametersGettersNames, List<Book> allRecordsBook) {

        for (int i = 0; i < variables.size(); i++) {
            String var = variables.get(i);
            String param = parametersGettersNames.get(i);
            System.out.println(var + " " + param);
            allRecordsBook = allRecordsBook.stream()
                    .filter(book -> book.allMethodsGetter(param, book).equals(var))
                    .collect(Collectors.toList());
        }

        allRecordsBook.sort(Comparator.comparing(Book::getID_book));

        return Parsers.parseListBookIntoListObject(allRecordsBook);
    }

    public static List<Object> filterBookOr(ArrayList<String> variables, ArrayList<String> parametersGettersNames, List<Book> allRecordsBook) {
        List<Book> allRecordsFiltered = new ArrayList<>();
        List<Book> tempRecordsFiltered;

        for (int i = 0; i < variables.size(); i++) {
            String var = variables.get(i);
            String param = parametersGettersNames.get(i);
            System.out.println(var + " " + param);
            tempRecordsFiltered = allRecordsBook.stream()
                    .filter(book -> book.allMethodsGetter(param, book).equals(var))
                    .collect(Collectors.toList());
            for (Book book : tempRecordsFiltered) {
                if (!allRecordsFiltered.contains(book))
                    allRecordsFiltered.add(book);
            }
        }

        allRecordsFiltered.sort(Comparator.comparing(Book::getID_book));

        return Parsers.parseListBookIntoListObject(allRecordsFiltered);
    }
}
