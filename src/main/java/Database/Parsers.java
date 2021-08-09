package Database;

import Data.Book;
import Data.User;
import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
        Gson gson = new Gson();
        String tmp = gson.toJson(object);
        return gson.fromJson(tmp, User.class);
    }

    public static Object parseUserIntoObject(User user) {
        Gson gson = new Gson();
        String tmp = gson.toJson(user);
        return gson.fromJson(tmp, Object.class);
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
        Gson gson = new Gson();
        String tmp = gson.toJson(object);
        return gson.fromJson(tmp, Book.class);
    }

    public static Object parseBookIntoObject(Book book) {
        Gson gson = new Gson();
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
}
