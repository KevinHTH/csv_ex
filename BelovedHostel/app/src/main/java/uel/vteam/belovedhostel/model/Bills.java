package uel.vteam.belovedhostel.model;

import java.util.List;

/**
 * Created by Hieu on 11/7/2016.
 */

public class Bills {
    private String billId;
    private String customerId;
    private Boolean isCheckOut;
    private Boolean isValid;
    private String checkIn;
    private String checkOut;
    private String dateCreate;
    private String amountPeople;
    private String contact;
    private String voucherNumber;
    private String totalPrice;
    private List<String> listRoom;

    public Bills() {
    }

    public Bills(String billId, String customerId, Boolean isCheckOut, Boolean isValid,
                 String checkIn, String checkOut, String dateCreate, String amountPeople,
                 String contact, String voucherNumber, String totalPrice, List<String> listRoom) {
        this.billId = billId;
        this.customerId = customerId;
        this.isCheckOut = isCheckOut;
        this.isValid = isValid;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.dateCreate = dateCreate;
        this.amountPeople = amountPeople;
        this.contact = contact;
        this.voucherNumber = voucherNumber;
        this.totalPrice = totalPrice;
        this.listRoom = listRoom;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Boolean getIsCheckOut(){
        return  isCheckOut;
    }

    public void setIsCheckOut(Boolean isCheckOut){
        this.isCheckOut=isCheckOut;
    }
    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getAmountPeople() {
        return amountPeople;
    }

    public void setAmountPeople(String amountPeople) {
        this.amountPeople = amountPeople;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<String> getListRoom() {
        return listRoom;
    }

    public void setListRoom(List<String> listRoom) {
        this.listRoom = listRoom;
    }
}