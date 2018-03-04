package sg.vinova.noticeboard.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cuong on 5/9/17.
 */

public class ImageObject {


    /**
     * id : 24
     * photo_url : http://noticeboard-staging.s3.amazonaws.com/staging/images/images/24_%20partition/original/maxresdefault.jpg?1494323343
     */

    @SerializedName("id")
    private int id;
    @SerializedName("photo_url")
    private String photoUrl;
    @SerializedName("_destroy")
    private int _destroy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl ==null ? "": photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public int get_destroy() {
        return _destroy;
    }

    public void set_destroy(int _destroy) {
        this._destroy = _destroy;
    }
}
