package com.zirhgrup.jobmanagement.model;

import java.util.Date;
import java.util.List;

public class Maintenance {
    private enum MaintenanceType{PERIODIC,BREAKDOWN}
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
        createTime = new Date().getTime();

    }

    public MaintenanceType getType() {
        return type;
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

    public class Part{
        private String partName;
        private String jobExplanation;

        public Part(String partName, String jobExplanation) {
            this.partName = partName;
            this.jobExplanation = jobExplanation;
        }

        public Part() {
        }

        public String getPartName() {
            return partName;
        }

        public String getJobExplanation() {
            return jobExplanation;
        }
    }
}

