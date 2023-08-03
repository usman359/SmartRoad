package com.ass2.smart_road;

import java.io.Serializable;

public class SignInPhoneNumberDetails implements Serializable {
    private String phoneNumber;

    public SignInPhoneNumberDetails() {

    }

    public SignInPhoneNumberDetails(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
