package sg.vinova.noticeboard.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ray on 3/6/17.
 */

public class BaseListObjectResponse<T> {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<T> list;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public List<T> getData() {
        return list;
    }
}
