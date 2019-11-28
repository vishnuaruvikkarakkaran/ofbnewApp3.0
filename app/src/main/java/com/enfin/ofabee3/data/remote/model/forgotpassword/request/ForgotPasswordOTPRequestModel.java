package com.enfin.ofabee3.data.remote.model.forgotpassword.request;

public class ForgotPasswordOTPRequestModel {
    /**
     * email : kiran.jb+123@enfintechnologies.com
     * hash_key :
     */

    private String email;
    private String hash_key;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash_key() {
        return hash_key;
    }

    public void setHash_key(String hash_key) {
        this.hash_key = hash_key;
    }
}
