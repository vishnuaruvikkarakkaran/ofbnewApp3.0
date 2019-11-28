package com.enfin.ofabee3.data.remote.model.Request;

public class OtpValidateRequestModel {
    /**
     * name : Kiran JB
     * phone : 9633770528
     * country_code : +91
     * password : 123456ab
     * email : kiran.jb+111@enfintechnologies.com
     * mode : 1
     * otp : 43905
     * identifier : 1
     */

    private String name;
    private String phone;
    private String country_code;
    private String password;
    private String email;
    private String mode;
    private String otp;
    private String identifier;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
