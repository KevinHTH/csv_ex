package sg.vinova.noticeboard.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jacky on 6/23/17.
 */

@Setter
@Getter
public class ClusterAdmin {
    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("company")
    private String company;

    @SerializedName("cea_license_number")
    private String cea_license_number;

    @SerializedName("cea_registration_number")
    private String cea_registration_number;

    @SerializedName("photo_url")
    private String photo_url;


}
