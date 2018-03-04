package sg.vinova.noticeboard.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by cuong on 4/27/17.
 */

@Getter
@Setter
public class ClusterResponse {


    /**
     * id : 1
     * name : Chung Cu Ha Kieu
     * code : 123456
     * api_token : CLSSl5DNdO8C6oCbdLKr5aZHyelq47q6Vhcqvg/dkY/qPRbvn7ctFnMxLSTrQtRVQw7sozv16OU7HqIvSEIO4taxg==
     */

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("code")
    private String code;

    @SerializedName("api_token")
    private String apiToken;

    @SerializedName("cluster")
    private Cluster cluster;




}
