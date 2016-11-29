package ecom.com.mshop.Utils;

/**
 * Created by Pandey on 18-11-2016.
 */
import com.google.gson.annotations.SerializedName;
public class RegisteredUser {

    private boolean status;

    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
