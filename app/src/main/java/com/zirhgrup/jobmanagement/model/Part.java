package com.zirhgrup.jobmanagement.model;

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
