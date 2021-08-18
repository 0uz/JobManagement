package com.zirhgrup.jobmanagement.model;

public class Customer extends User{
    Role role;
    public Customer(String name, String surname, String email, String phoneNo) {
        super(name, surname, email, phoneNo);
        role = Role.CUSTOMER;
    }

    public Role getRole() {
        return role;
    }

    public Customer() {
    }
}
