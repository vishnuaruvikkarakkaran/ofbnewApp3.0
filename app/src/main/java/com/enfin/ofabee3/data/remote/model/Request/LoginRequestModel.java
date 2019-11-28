package com.enfin.ofabee3.data.remote.model.Request;

public class LoginRequestModel {

    /**
     * phone : 9633770528
     * email : kiran.jb+123@enfintechnologies.com
     * mode : 2
     * password : 123456ab
     */

    private String phone;
    private String email;
    private String mode;
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
