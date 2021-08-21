package com.zirhgrup.jobmanagement.model;

public class Admin extends User{
    public Admin() {
    }

    public Admin(String name, String surname, String email, String phoneNo) {
        super(name, surname, email, phoneNo,Role.ADMIN);
    }
}
