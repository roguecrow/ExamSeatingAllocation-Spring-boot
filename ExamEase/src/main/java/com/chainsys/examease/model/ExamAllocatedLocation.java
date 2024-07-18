package com.chainsys.examease.model;

public class ExamAllocatedLocation {
    private int allocatedSeat;
    private int locationId;
    private String city;
    private String venueName;
    private String hallName;
    private int totalCapacity;
    private String address;
    private String locationUrl;
    private int examId;
    private int filledCapacity;
    private String serialNo;


    public int getAllocatedSeat() {
        return allocatedSeat;
    }

    public int getLocationId() {
        return locationId;
    }

    public String getCity() {
        return city;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getHallName() {
        return hallName;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public String getAddress() {
        return address;
    }

    public String getLocationUrl() {
        return locationUrl;
    }

    public int getExamId() {
        return examId;
    }

    public int getFilledCapacity() {
        return filledCapacity;
    }

    public String getSerialNo() {
        return serialNo;
    }


    public void setAllocatedSeat(int allocatedSeat) {
        this.allocatedSeat = allocatedSeat;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLocationUrl(String locationUrl) {
        this.locationUrl = locationUrl;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public void setFilledCapacity(int filledCapacity) {
        this.filledCapacity = filledCapacity;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }


    @Override
    public String toString() {
        return "LocationDetails{" + "allocatedSeat=" + allocatedSeat + ", locationId=" + locationId + ", city='" + city
                + '\'' + ", venueName='" + venueName + '\'' + ", hallName='" + hallName + '\'' + ", totalCapacity="
                + totalCapacity + ", address='" + address + '\'' + ", locationUrl='" + locationUrl + '\'' + ", examId="
                + examId + ", filledCapacity=" + filledCapacity + '}';
    }
}
