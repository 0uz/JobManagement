package com.zirhgrup.jobmanagement.model;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.zirhgrup.jobmanagement.database.DatabaseLayer;

import java.util.Date;
import java.util.List;

public class Service extends User {
    private boolean isBanned;
    private long createTime;


    public Service() {
    }

    public Service(String name, String surname, String email, String phoneNo, boolean isBanned) {
        super(name, surname, email, phoneNo,Role.SERVICE);
        this.isBanned = isBanned;
        this.createTime = new Date().getTime();

    }


    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public long getCreateTime() {
        return createTime;
    }
}
