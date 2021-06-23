package com.chery.exeed.crm.lead.dto;

import java.util.Date;

//临时试驾车型报表实体类
public class TemporaryTestDriveDTO {
    String name;//姓名
    Integer age;//年龄
    String gender;//性别
    String phone;//手机号
    String isDealerPersonnel;//是否经销商人员
    String dealerIsTestDriveGifts;//经销商是否试驾赠礼
    String modelOfInterest;//意向车型
    String purchaseIntention;//意向选购方式
    String occupation;//职业
    Date testDriveTime;//试驾时间
    String dealerCode;//经销商编号
    String dealerName;//经销商名字

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsDealerPersonnel() {
        return isDealerPersonnel;
    }

    public void setIsDealerPersonnel(String isDealerPersonnel) {
        this.isDealerPersonnel = isDealerPersonnel;
    }

    public String getDealerIsTestDriveGifts() {
        return dealerIsTestDriveGifts;
    }

    public void setDealerIsTestDriveGifts(String dealerIsTestDriveGifts) {
        this.dealerIsTestDriveGifts = dealerIsTestDriveGifts;
    }

    public String getModelOfInterest() {
        return modelOfInterest;
    }

    public void setModelOfInterest(String modelOfInterest) {
        this.modelOfInterest = modelOfInterest;
    }

    public String getPurchaseIntention() {
        return purchaseIntention;
    }

    public void setPurchaseIntention(String purchaseIntention) {
        this.purchaseIntention = purchaseIntention;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Date getTestDriveTime() {
        return testDriveTime;
    }

    public void setTestDriveTime(Date testDriveTime) {
        this.testDriveTime = testDriveTime;
    }

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }
}
