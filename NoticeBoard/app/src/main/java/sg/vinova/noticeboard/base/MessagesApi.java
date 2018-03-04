package sg.vinova.noticeboard.base;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jacky on 3/21/2017.
 * "status": true,
 * "data": {
 * "message": "Reset Password success!",
 * "messages": []
 }
 */
@Getter
@Setter
@Keep
public class MessagesApi {



    @SerializedName("message")
    private String message;

    @SerializedName("messages")
    private List messages;





}
