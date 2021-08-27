package com.zirhgrup.jobmanagement.model;

public class Customer extends User{
    String ownerSerial;
    public Customer(String name, String surname, String email, String phoneNo,String ownerSerial) {
        super(name, surname, email, phoneNo,Role.CUSTOMER);
        this.ownerSerial = ownerSerial;
    }

    public String getOwnerSerial() {
        return ownerSerial;
    }

    public Customer() {
    }
}
