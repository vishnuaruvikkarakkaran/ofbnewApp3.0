package com.enfin.ofabee3.data.remote.model.Request;

public class RegistrationRequestModel {
    /**
     * name : Kiran JB
     * phone : 9633770528
     * country_code : +91
     * email : kiran.jb+112@enfintechnologies.com
     * password : 123456ab
     * hash_key :
     */

    private String name;
    private String phone;
    private String country_code;
    private String email;
    private String password;
    private String hash_key;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHash_key() {
        return hash_key;
    }

    public void setHash_key(String hash_key) {
        this.hash_key = hash_key;
    }
}
