package com.enfin.ofabee3.data.remote.model.Request;

public class OtpRequestModel {
    /**
     * phone : 9633770528
     * email : kiran.jb+123@enfintechnologies.com
     * mode : 2
     * hash_key :
     */

    private String phone;
    private String email;
    private String mode;
    private String hash_key;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getHash_key() {
        return hash_key;
    }

    public void setHash_key(String hash_key) {
        this.hash_key = hash_key;
    }
}
