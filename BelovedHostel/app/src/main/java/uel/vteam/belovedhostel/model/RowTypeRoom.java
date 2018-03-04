package uel.vteam.belovedhostel.model;

/**
 * Created by Hieu on 11/13/2016.
 */

public class RowTypeRoom {
    private String title;
    private int icon;
    private  String detail;

    public RowTypeRoom(String title, int icon, String detail) {
        this.title = title;
        this.icon = icon;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
