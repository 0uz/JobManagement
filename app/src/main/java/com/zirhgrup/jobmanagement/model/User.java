package com.zirhgrup.jobmanagement.model;

public class User {
    public enum Role{
        ADMIN,
        SERVICE,
        CUSTOMER
    }
    private String name;
    private String surname;
    private String email;
    private String phoneNo;
    private Role role;


    public User(String name, String surname, String email, String phoneNo, Role role) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNo = phoneNo;
        this.role = role;
    }

    public User() {
    }

    public Role getRole() {
        return role;
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
