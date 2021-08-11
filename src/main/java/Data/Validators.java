package Data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Validators {

    public static boolean resourceExistence(String table, Integer id, Connection connection) {
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

    public static boolean tableExistence(String table, Connection connection) {
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

    public static boolean fieldsValidation (Object object, ArrayList<String> fields) {
        Gson gson = new Gson();
        String tmp = gson.toJson(object);
        System.out.println(tmp);

        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String,Object> map = gson.fromJson(tmp, type);

        //iterate the map in order to get field name and value
        Set<String> fieldSet = map.keySet();
        for (String fieldName : fieldSet) {
            if (!fields.contains(fieldName))
                return false;
        }
        return true;
    }

}
