package sg.vinova.noticeboard.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray on 7/17/17.
 */

public class BlockedUsers {
    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("phone_number")
    private String phone_number;


    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username == null ? "" : username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone_number() {
        return phone_number == null ? "" : phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
