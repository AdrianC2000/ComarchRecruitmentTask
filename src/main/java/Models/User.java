package Models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class User {
    private Integer ID_user;

    @NotBlank
    private String login;

    @NotBlank
    @Email(message = "Incorrect email format.")
    private String email;

    @NotBlank
    private String first_name;

    @NotBlank
    private String last_name;

    private String creation_date;

    public Integer getID_user() {
        return ID_user;
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
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
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
