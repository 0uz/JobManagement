package com.zirhgrup.jobmanagement.model;

public class Admin extends User{
    Role role;

    public Admin() {
    }

    public Admin(String name, String surname, String email, String phoneNo) {
        super(name, surname, email, phoneNo);
        role = Role.ADMIN;
    }
}
