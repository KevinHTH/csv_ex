package sg.vinova.noticeboard.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by cuong on 6/7/17.
 */

@Getter
@Setter
public class Cluster {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("company")
    private String company;

    @SerializedName("cea_license_number")
    private String ceaLicenseNumber;

    @SerializedName("cea_registration_number")
    private String ceaRegistrationNumber;

    @SerializedName("cluster_admin")
    private ClusterAdmin clusterAdmin;
}
