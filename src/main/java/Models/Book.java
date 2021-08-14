package Models;

import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Book {

    private Integer ID_book;

    @Size(min = 1, max = 50, message = "Size error: Title should have more than 0 signs and less than 50 signs.")
    private String title;

    @Size(min = 1, max = 50, message = "Size error: Author should have more than 0 signs and less than 50 signs.")
    @Pattern(regexp = "^[\\s\\p{L}]+$", message = "Format error: Author can only contain letters and spaces.")
    private String author;

    @Pattern(regexp = "^(1|0)$", message = "Format error: Is taken has to be bool (0 / 1).")
    private String is_taken;

/*
    @Min(value = 1, message = "Format error: Taken by has to be a positive integer.")
*/
    private Integer taken_by;

    @PastOrPresent(message = "Format error: Creation date must be past or present.")
    private Date taken_date;

    private Date return_date;

    public Integer getID_book() {
        return ID_book;
    }

    public void setID_book(String ID_book) {
        this.ID_book = Integer.valueOf(ID_book);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getIs_taken() {
        return (is_taken.equals("1"));
    }

    public void setIs_taken(String is_taken) {
        this.is_taken = is_taken;
    }

    public Integer getTaken_by() {
        return taken_by;
    }

    public void setTaken_by(String taken_by) {
        try {
            this.taken_by = Integer.valueOf(taken_by);
        }
        catch (NumberFormatException e) {
            this.taken_by = 0;
        }
    }

    public String getTaken_date() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(taken_date);
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public void setTaken_date(String taken_date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            this.taken_date = formatter.parse(taken_date);
        }
        catch (ParseException | NullPointerException e) {
            this.taken_date = null;
        }
    }

    public String getReturn_date() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(return_date);
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public void setReturn_date(String return_date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            this.return_date = formatter.parse(return_date);
        }
        catch (ParseException | NullPointerException e) {
            this.return_date = null;
        }
    }

    public String allMethodsGetter(String methodName, Book requirements) {
        try {
            Method method = Book.class.getDeclaredMethod(methodName);
            if (!methodName.contains("Is"))
                return (String) method.invoke(requirements);
            else
                return (Boolean) method.invoke(requirements) ? "true" : "false";
        }
        catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            return null;
        }
    }
}
