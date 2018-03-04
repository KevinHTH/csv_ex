package uel.vteam.belovedhostel.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hieu on 11/7/2016.
 */

public class MyRoom implements Serializable {

    private String roomId;
    private String roomPrice;
    private List<MyImage>roomImage;
    private String roomAcreage;
    private String roomFurniture;
    private  String bedRoom;
    private  String discount;
    private String isBooked;

    public MyRoom() {
    }

    public MyRoom(String roomId, String roomPrice, List<MyImage> roomImage, String roomAcreage,
                  String roomFurniture, String bedRoom, String discount, String isBooked) {
        this.roomId = roomId;
        this.roomPrice = roomPrice;
        this.roomImage = roomImage;
        this.roomAcreage = roomAcreage;
        this.roomFurniture = roomFurniture;
        this.bedRoom = bedRoom;
        this.discount = discount;
        this.isBooked=isBooked;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public List<MyImage> getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(List<MyImage> roomImage) {
        this.roomImage = roomImage;
    }

    public String getRoomAcreage() {
        return roomAcreage;
    }

    public void setRoomAcreage(String roomAcreage) {
        this.roomAcreage = roomAcreage;
    }

    public String getRoomFurniture() {
        return roomFurniture;
    }

    public void setRoomFurniture(String roomFurniture) {
        this.roomFurniture = roomFurniture;
    }

    public String getBedRoom() {
        return bedRoom;
    }

    public void setBedRoom(String bedRoom) {
        this.bedRoom = bedRoom;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(String isBooked) {
        this.isBooked = isBooked;
    }
}

