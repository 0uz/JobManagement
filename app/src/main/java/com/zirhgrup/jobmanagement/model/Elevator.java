package com.zirhgrup.jobmanagement.model;

import com.google.firebase.firestore.GeoPoint;





public class Elevator {

    public enum ElevatorType{HORIZONTAL,STAIRS}
    public enum PaintingType {STATIC, STAINLESS}
    public enum WorkingEngine {TWO,FIVE}
    public enum WorkingCapacity{KG125,KG225,KG350}

    String photoURL1, photoURL2;
    String serialNo;
    String elevatorInfo;
    ElevatorType type;
    PaintingType paintingType;
    Double height,width,workHeight;
    WorkingEngine motor;
    WorkingCapacity capacity;
    GeoPoint point;

    public Elevator(String serialNo, String elevatorInfo, ElevatorType type, PaintingType paintingType, Double height, Double width, Double workHeight, WorkingEngine motor, WorkingCapacity capacity, GeoPoint point) {
        this.photoURL1 = photoURL1;
        this.photoURL2 = photoURL2;
        this.serialNo = serialNo;
        this.elevatorInfo = elevatorInfo;
        this.type = type;
        this.paintingType = paintingType;
        this.height = height;
        this.width = width;
        this.workHeight = workHeight;
        this.motor = motor;
        this.capacity = capacity;
        this.point = point;
    }

    public Elevator() {

    }

    public String getElevatorInfo() {
        return elevatorInfo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public ElevatorType getType() {
        return type;
    }

    public PaintingType getPaintingType() {
        return paintingType;
    }

    public Double getHeight() {
        return height;
    }

    public Double getWidth() {
        return width;
    }

    public Double getWorkHeight() {
        return workHeight;
    }

    public WorkingEngine getMotor() {
        return motor;
    }

    public WorkingCapacity getCapacity() {
        return capacity;
    }

    public GeoPoint getPoint() {
        return point;
    }

    public String getPhotoURL1() {
        return photoURL1;
    }

    public String getPhotoURL2() {
        return photoURL2;
    }

    public void setPhotoURL1(String photoURL1) {
        this.photoURL1 = photoURL1;
    }

    public void setPhotoURL2(String photoURL2) {
        this.photoURL2 = photoURL2;
    }
}
