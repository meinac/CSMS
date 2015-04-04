package com.csms.mehmetinac.csms;

/**
 * Created by mehmetinac on 28/01/15.
 */
public class SMS {

    public int id;
    public String phoneNumber;
    public String message;

    public SMS() {}

    public SMS(int id, String phoneNumber, String message) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    public SMS(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

}
