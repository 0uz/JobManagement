package com.zirhgrup.jobmanagement.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.List;


public class Elevator {

    public enum ElevatorType{HORIZONTAL,STAIRS}
    public enum PaintingType {STATIC, STAINLESS}
    public enum WorkingEngine {TWO,FIVE}
    public enum WorkingCapacity{KG125,KG225,KG350}

    private String photoURL1, photoURL2;
    private String serialNo;
    private String elevatorInfo;
    private ElevatorType type;
    private PaintingType paintingType;
    private Double height,width,workHeight;
    private WorkingEngine engine;
    private WorkingCapacity capacity;
    private GeoPoint point;
    private long createTime;
    private long nextMaintenanceTime;
    @Exclude
    private List<Maintenance> maintenances;
    @Exclude
    private Customer customer;

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
        this.engine = motor;
        this.capacity = capacity;
        this.point = point;
        this.createTime = new Date().getTime();
        this.nextMaintenanceTime = createTime + 7889229;
    }

    public Elevator() {

    }

    @Exclude
    public Customer getCustomer() {
        return customer;
    }
    @Exclude
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    @Exclude
    public List<Maintenance> getMaintenances() {
        return maintenances;
    }
    @Exclude
    public void setMaintenances(List<Maintenance> maintenances) {
        this.maintenances = maintenances;
    }

    public long getNextMaintenanceTime() {
        return nextMaintenanceTime;
    }

    public long getCreateTime() {
        return createTime;
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

    public WorkingEngine getEngine() {
        return engine;
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
