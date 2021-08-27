package com.zirhgrup.jobmanagement.model;

import android.content.Context;
import android.view.View;
import com.zirhgrup.jobmanagement.R;

import java.util.Date;
import java.util.List;

public class Maintenance {
    public enum MaintenanceType{PERIODIC,CUSTOM}
    private long createTime;
    private MaintenanceType type;
    private String ownerServiceEmail;
    private String jobExplanation;
    private List<Part> changedParts;

    public Maintenance() {
    }

    public Maintenance(MaintenanceType type, String ownerServiceEmail, String jobExplanation, List<Part> changedParts) {
        this.type = type;
        this.ownerServiceEmail = ownerServiceEmail;
        this.jobExplanation = jobExplanation;
        this.changedParts = changedParts;
        createTime = new Date().getTime()/1000;

    }

    public MaintenanceType getType() {
        return type;
    }

    public String formattedType(Context view){
        if (type.equals(MaintenanceType.PERIODIC)) return view.getResources().getString(R.string.periodicMaintenance);
        else return view.getResources().getString(R.string.customMaintenance);
    }

    public String getOwnerServiceEmail() {
        return ownerServiceEmail;
    }

    public String getJobExplanation() {
        return jobExplanation;
    }

    public List<Part> getChangedParts() {
        return changedParts;
    }

    public long getCreateTime() {
        return createTime;
    }
}
