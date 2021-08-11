package Data;

import java.util.List;

public class ReturnMessage {
    private String message;
    private List<Object> result;
    private boolean isValid;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Object> getResult() {
        return result;
    }

    public void setResult(List<Object> result) {
        this.result = result;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public ReturnMessage(String message, List<Object> result, boolean isValid) {
        this.message = message;
        this.result = result;
        this.isValid = isValid;
    }
}
