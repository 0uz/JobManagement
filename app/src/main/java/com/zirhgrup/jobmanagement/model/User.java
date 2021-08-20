package com.zirhgrup.jobmanagement.model;

public abstract class User {
    public enum Role{
        ADMIN,
        SERVICE,
        CUSTOMER
    }
    private String name;
    private String surname;
    private String email;
    private String phoneNo;


    public User(String name, String surname, String email, String phoneNo) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNo = phoneNo;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }
}
