package sg.vinova.noticeboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by cuong on 4/27/17.
 */

@Getter
@Setter
public class LoginResponse {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("cluster")
    @Expose
    private ClusterResponse clusterResponse;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("create_at")
    @Expose
    private String createAt;

    @SerializedName("api_token")
    @Expose
    private String api_token;

    @SerializedName("bulk_status")
    private String bulk_status;

    @SerializedName("bulk_sent_at")
    private String bulk_sent_at;

    @SerializedName("bulk_confirm_at")
    private String bulk_confirm_at;

    @SerializedName("created_at")
    private String created_at;


}
