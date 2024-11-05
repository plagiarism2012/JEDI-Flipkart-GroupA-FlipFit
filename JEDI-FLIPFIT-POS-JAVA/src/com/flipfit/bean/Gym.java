package com.flipfit.bean;

public class Gym {
    private int gymId;
    private String gymCentreID;
    private String ownerID;
    private String gymCenterName;
    protected String gstin;
    protected String pan;
    private String address;
    private int capacity;

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public int getGymId() {
        return gymId;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }

    public String getGymCentreID() {
        return gymCentreID;
    }

    public void setGymCentreID(String gymCentreID) {
        this.gymCentreID = gymCentreID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getGymCenterName() {
        return gymCenterName;
    }

    public void setGymCenterName(String gymCenterName) {
        this.gymCenterName = gymCenterName;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
