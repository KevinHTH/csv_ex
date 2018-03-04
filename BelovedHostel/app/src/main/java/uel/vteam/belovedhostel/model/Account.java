package uel.vteam.belovedhostel.model;

import java.io.Serializable;

/**
 * Created by Hieu on 10/30/2016.
 */

public class Account implements Serializable {
    private String userId;
    private String userName;
    private String userPassword;
    private String userPhone;
    private String userEmail;
    private String permission;

    //
    private String status;
    private String userAvatar;
    //
    private String identityNumber;
    private String visaId;
    private String masterCardId;
    private String passportId;
    private String countryPhoneCode;


    public Account() {
    }

    public Account(String userId, String userName, String userPassword, String userPhone, String userEmail,
                   String permission, String status, String userAvatar, String identityNumber, String visaId,
                   String masterCardId, String passportId, String countryPhoneCode) {
        this.userId=userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.permission=permission;
        this.status = status;
        this.userAvatar = userAvatar;
        this.identityNumber = identityNumber;
        this.visaId = visaId;
        this.masterCardId = masterCardId;
        this.passportId = passportId;
        this.countryPhoneCode = countryPhoneCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getVisaId() {
        return visaId;
    }

    public void setVisaId(String visaId) {
        this.visaId = visaId;
    }

    public String getMasterCardId() {
        return masterCardId;
    }

    public void setMasterCardId(String masterCardId) {
        this.masterCardId = masterCardId;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public String getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public void setCountryPhoneCode(String countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
