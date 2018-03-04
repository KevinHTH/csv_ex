package uel.vteam.belovedhostel.model;

import java.util.ArrayList;

/**
 * Created by Hieu on 12/26/2016.
 */

public class MyFolderImages {
    String folderName;
    String folderPath;
    ArrayList<MyImage> listImage;



    public MyFolderImages() {
    }

    public MyFolderImages(String folderName, String folderPath, ArrayList<MyImage> listImage) {
        this.folderName = folderName;
        this.folderPath = folderPath;
        this.listImage = listImage;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public ArrayList<MyImage> getListImage() {
        return listImage;
    }

    public void setListImage(ArrayList<MyImage> listImage) {
        this.listImage = listImage;
    }
}
