package Models;

import javax.validation.constraints.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    private Integer ID_user;

    @Pattern(regexp = "^\\S+$", message = "Format error: Login cannot contain spaces and cannot be empty.")
    private String login;

    @Pattern(regexp = "^\\S+$", message = "Format error: Email cannot contain spaces and cannot empty.")
    @Pattern(regexp = "^[\\w]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Format error: Email cannot contain spaces or is empty.")
    /*@Email(message = "Format error: Incorrect email format.") this is not always valid */
    private String email;

    @Pattern(regexp = "^[a-zA-Z]+$", message = "Format error: First name can only contain letters and cannot empty.")
    private String first_name;


    @Pattern(regexp = "^[a-zA-Z]+$", message = "Format error: Last name can only contain letters and cannot be empty.")
    private String last_name;

    @PastOrPresent (message = "Format error: Creation date must be past or present.")
    private Date creation_date;


    public User() throws ParseException {
    }

    public Integer takeID_userInt() {
        return ID_user;
    }

    public String getID_user() {
        return ID_user.toString();
    }

    public void setID_user(String ID_user) {
        this.ID_user = Integer.valueOf(ID_user);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCreation_date() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(creation_date);
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public void setCreation_date(String creation_date) {
        /*if (!Validators.validateJavaDate(creation_date)) {
        }*/
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            this.creation_date = formatter.parse(creation_date);
        }
        catch (ParseException | NullPointerException e) {
            this.creation_date = null;
        }
    }

    public String allMethodsGetter(String methodName, User requirements) {
        try {
            Method method = User.class.getDeclaredMethod(methodName);
            return (String) method.invoke(requirements);
        }
        catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            return null;
        }
    }
}
