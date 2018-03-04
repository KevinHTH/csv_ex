package sg.vinova.noticeboard.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Ray on 3/6/17.
 */

@Getter
@Setter
public class BaseObjectResponse<T> {
    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("data")
    @Expose
    private T t;

    @SerializedName("message")
    @Expose
    private String message;

    public boolean isStatus() {
        return status;
    }

    public T getData() {
        return t;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }
}
