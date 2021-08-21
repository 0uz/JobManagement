package com.zirhgrup.jobmanagement.model;

public class Customer extends User{

    public Customer(String name, String surname, String email, String phoneNo) {
        super(name, surname, email, phoneNo,Role.CUSTOMER);
    }

    public Customer() {
    }
}
