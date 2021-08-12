package Data;

import Models.Book;
import Models.User;
import com.google.gson.*;
import org.gradle.internal.impldep.org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.jboss.resteasy.spi.UnhandledException;

import javax.validation.ValidationException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class Parsers {

    public static List<Object> parseIntoObjectList(String tableName, ResultSet result) {
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
                        Method method = Book.class.getDeclaredMethod(methodName, String.class);
                        method.invoke(actualBook, columnValue);
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

    public static User parseObjectIntoUser(Object object) {
        try {
            Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
            String tmp = gson.toJson(object);
            return gson.fromJson(tmp, User.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ValidationException("Wrong date format.");
        }
    }

    public static Object parseUserIntoObject(User user) {
        try {
            Gson gson = new GsonBuilder().
                    registerTypeAdapter(Double.class,  new JsonSerializer<Double>() {
                        @Override
                        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                            if(src == src.longValue())
                                return new JsonPrimitive(src.longValue());
                            return new JsonPrimitive(src);
                        }

                    }).serializeNulls().setDateFormat("yyyy-MM-dd").create();
            String tmp = gson.toJson(user);
            return gson.fromJson(tmp, Object.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ValidationException("Wrong date format.");
        }
    }

    public static List<User> parseListObjectIntoListUser(List<Object> listObject) {
        List<User> listUser = new ArrayList<>();
        for (Object i : listObject) {
            User newUser = parseObjectIntoUser(i);
            listUser.add(newUser);
        }
        return listUser;
    }

    public static List<Object> parseListUserIntoListObject(List<User> listUser) {
        List<Object> listObject = new ArrayList<>();
        for (User i : listUser) {
            Object newObject = parseUserIntoObject(i);
            listObject.add(newObject);
        }
        return listObject;
    }

    public static Book parseObjectIntoBook(Object object) {
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").serializeNulls().create();
            String tmp = gson.toJson(object);
            return gson.fromJson(tmp, Book.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidationException("Wrong date format.");
        }
    }

    public static Object parseBookIntoObject(Book book) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").serializeNulls().create();
        String tmp = gson.toJson(book);
        return gson.fromJson(tmp, Object.class);
    }

    public static List<Book> parseListObjectIntoListBook(List<Object> listObject) {
        List<Book> listBook = new ArrayList<>();
        for (Object i : listObject) {
            Book newBook = parseObjectIntoBook(i);
            listBook.add(newBook);
        }
        return listBook;
    }

    public static List<Object> parseListBookIntoListObject(List<Book> listBook) {
        List<Object> listObject = new ArrayList<>();
        for (Book i : listBook) {
            Object newObject = parseBookIntoObject(i);
            listObject.add(newObject);
        }
        return listObject;
    }

    public static ArrayList<String> parseFieldsArrayIntoStringList(Field[] fields) {
        ArrayList<String> stringList = new ArrayList<>();
        for (Field field : fields) {
            stringList.add(field.getName());
        }
        return stringList;
    }

    public static String resourceName(String tableName) {
        // Example: tableName: users, resourceName = User
        String resource = tableName.substring(0, tableName.length() - 1);
        return resource.substring(0, 1).toUpperCase() + resource.substring(1);
    }
}
