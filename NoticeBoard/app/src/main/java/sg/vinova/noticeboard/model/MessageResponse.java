package sg.vinova.noticeboard.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray on 7/17/17.
 */

public class MessageResponse {

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
