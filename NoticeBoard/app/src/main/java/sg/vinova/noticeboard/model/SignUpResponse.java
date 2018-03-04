package sg.vinova.noticeboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jacky on 6/23/17.
 */

@Setter
@Getter
public class SignUpResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("cluster")
    private Cluster cluster;

    @SerializedName("email")
    private String email;

    @SerializedName("role")
    private String role;

    @SerializedName("bulk_status")
    private String bulk_status;

    @SerializedName("bulk_sent_at")
    private String bulk_sent_at;

    @SerializedName("bulk_confirm_at")
    private String bulk_confirm_at;

    @SerializedName("created_at")
    private String created_at;




}
