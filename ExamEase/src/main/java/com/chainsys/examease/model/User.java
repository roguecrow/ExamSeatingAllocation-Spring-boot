package com.chainsys.examease.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.context.annotation.Configuration;
@Configuration
public class User implements Serializable {
	
	private int rollNo;
    private String name;
    private int roleId;
    private String username;
    private Date dob;
    private String qualification;
    private char gender;
    private String cityPreference1;
    private String cityPreference2;
    private String cityPreference3;
    private String address;
    private String nativeCity;
    private String state;
    private String allocatedCity;
    private int serialNo;
    private String aadharNumber;
    private byte[] passportSizePhoto;  
    private byte[] digitalSignature;  
    private byte[] qualificationDocuments; 
    private String password;
    private String email;

   

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getCityPreference1() {
        return cityPreference1;
    }

    public void setCityPreference1(String cityPreference1) {
        this.cityPreference1 = cityPreference1;
    }

    public String getCityPreference2() {
        return cityPreference2;
    }

    public void setCityPreference2(String cityPreference2) {
        this.cityPreference2 = cityPreference2;
    }

    public String getCityPreference3() {
        return cityPreference3;
    }

    public void setCityPreference3(String cityPreference3) {
        this.cityPreference3 = cityPreference3;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNativeCity() {
        return nativeCity;
    }

    public void setNativeCity(String nativeCity) {
        this.nativeCity = nativeCity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAllocatedCity() {
        return allocatedCity;
    }

    public void setAllocatedCity(String allocatedCity) {
        this.allocatedCity = allocatedCity;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public byte[] getPassportSizePhoto() {
        return passportSizePhoto;
    }

    public void setPassportSizePhoto(byte[] passportSizePhoto) {
        this.passportSizePhoto = passportSizePhoto;
    }

    public byte[] getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(byte[] digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public byte[] getQualificationDocuments() {
        return qualificationDocuments;
    }

    public void setQualificationDocuments(byte[] qualificationDocuments) {
        this.qualificationDocuments = qualificationDocuments;
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
