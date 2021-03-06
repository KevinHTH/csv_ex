package uel.vteam.belovedhostel.model;

/**
 * Created by Hieu on 11/18/2016.
 */

public class CountryPhoneCode {
    private String name;
    private String dial_code;
    private String code;

    public CountryPhoneCode() {

    }

    public CountryPhoneCode(String name, String dial_code, String code) {
        this.name = name;
        this.dial_code = dial_code;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDial_code() {
        return dial_code;
    }

    public void setDial_code(String dial_code) {
        this.dial_code = dial_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "\n"+getName()+"\n\n\n"+getDial_code();
    }
}
