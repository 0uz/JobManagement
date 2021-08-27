package com.zirhgrup.jobmanagement.model;

import androidx.room.*;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.List;

@Entity
public class Elevator {

    public enum ElevatorType{HORIZONTAL,STAIRS}
    public enum PaintingType {STATIC, STAINLESS}
    public enum WorkingEngine {TWO,FIVE}
    public enum WorkingCapacity{KG125,KG225,KG350}
    private String photoURL1, photoURL2;
    private String serialNo;
    private String elevatorInfo;
    private ElevatorType elevatorType;
    private PaintingType paintingType;
    private Double height,width,workHeight;
    private WorkingEngine engine;
    private WorkingCapacity capacity;
    private GeoPoint point;
    private long createTime;
    private long nextMaintenanceTime;
    private List<Maintenance> maintenances;
    @Exclude
    private Customer customer;
    @Exclude
    private User ownerData;
    private String owner;

    public Elevator(String serialNo, String elevatorInfo, ElevatorType type, PaintingType paintingType, Double height, Double width, Double workHeight, WorkingEngine motor, WorkingCapacity capacity, GeoPoint point, String user) {
        this.serialNo = serialNo;
        this.elevatorInfo = elevatorInfo;
        this.elevatorType = type;
        this.paintingType = paintingType;
        this.height = height;
        this.width = width;
        this.workHeight = workHeight;
        this.engine = motor;
        this.capacity = capacity;
        this.point = point;
        this.owner = user;
        this.createTime = new Date().getTime()/1000;
        this.nextMaintenanceTime = createTime + 7889229;
    }

    public Elevator() {

    }

    public String getOwner() {
        return owner;
    }


    @Exclude
    public User getOwnerData() {
        return ownerData;
    }
    @Exclude
    public void setOwnerData(User ownerData) {
        this.ownerData = ownerData;
    }

    @Exclude
    public Customer getCustomer() {
        return customer;
    }
    @Exclude
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    //TODO TEST
    public List<Maintenance> getMaintenances() {
        return maintenances;
    }

    public void setMaintenances(List<Maintenance> maintenances) {
        this.maintenances = maintenances;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
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

    public ElevatorType getElevatorType() {
        return elevatorType;
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

    public String formatType(){
        return elevatorType.toString().substring(0,1) + elevatorType.toString().substring(1).toLowerCase();
    }
    public String formatPainting(){
        return paintingType.toString().substring(0,1) + paintingType.toString().substring(1).toLowerCase();
    }
    public String formatEngine(){
        if (engine == WorkingEngine.TWO) return "2";
        else return "5";
    }
    public String formatCapacity(){
        if (capacity == WorkingCapacity.KG125) return "125kg";
        else if(capacity == WorkingCapacity.KG225) return "225kg";
        else return "350kg";
    }
}
