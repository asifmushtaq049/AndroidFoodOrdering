package com.uog.smartcafe.entities;


public class LoginObject {

    private int id;
    private String username;
    private String email;
    private String address;
    private String phone;
    private String balance;
    private String loggedIn;

    public LoginObject(int id, String username, String email, String address, String phone ,String  balance ,String loggedIn) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.balance = balance;
        this.loggedIn = loggedIn;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getBalance() {
        return balance;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getLoggedIn() {
        return loggedIn;
    }
}
