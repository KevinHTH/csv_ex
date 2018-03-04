package sg.vinova.noticeboard.model;

import android.support.annotation.Keep;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jacky on 28/4/17.
 */

@Keep

public class Photo {
    private String id;
    private String path;
    private boolean isSelected = false;
    private int destroy;



    public Photo(String path, boolean isSelected) {
        this.path = path;
        this.isSelected = isSelected;
    }

    public Photo(String path) {
        this.path = path;
    }

    public Photo(String id, String path) {
        this.id = id;
        this.path = path;
    }

    public Photo() {
    }


    public String getPath() {
        return path == null ? "" : path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getDestroy() {
        return destroy;
    }

    public void setDestroy(int destroy) {
        this.destroy = destroy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
