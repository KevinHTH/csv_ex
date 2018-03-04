package uel.vteam.belovedhostel.model;

import java.io.Serializable;

/**
 * Created by Hieu on 11/13/2016.
 */

public class MyImage implements Serializable {
    String imageName;
    String imageLink;

    public MyImage() {

    }

    public MyImage(String imageName, String imageLink) {

        this.imageName = imageName;
        this.imageLink = imageLink;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}