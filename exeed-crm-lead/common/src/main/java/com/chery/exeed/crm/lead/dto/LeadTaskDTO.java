package com.chery.exeed.crm.lead.dto;

import java.io.Serializable;
import java.util.Date;

public class LeadTaskDTO implements Serializable {
    private Long id;

    private String phone;

    private String custName;

    private String isSpecialCustomer;

    private Integer leadId;

    private Integer custId;

    private Integer taskType;

    private Integer owner;

    private String ownerName;

    private Integer status;

    private Date dueDatePlan;

    private Date customerCarDate;

    private Date dueDateActual;

    private Integer taskResult;
    private String statusDesc;
    private String taskResultDesc;
    private String taskTypeDesc;

    private Date firstCallTime;

    private String nextCallTime;

    private Integer callTimes;

    private String memo;


    /**线索信息*/
    private String lastName;

    private String firstName;

    private Integer rating;
    /**采购预算*/
    private String budget;
    /**购车日期*/
    private String purchaseTime;

    private String dealer;

    private String dealerName;

    private String interiorYearlyBudget;

    /**咨询频次*/
    private Integer consultingFrequency;

    private String brandName;

    private String seriesName;

    private String modelName;

    private String colorName;

    private String province;

    private String city;

    private Integer gender;

    private String subCity;
    /**购车用途*/
    private String vehiclePurpose;
    /**预约到店时间*/
    private Date testDrivePlan;

    private String hobby;

    private String persontitle;

    private Integer age;

    private String followCar;

    private String followInfo;

    private String followRemarks;

    private Date modifyTime;

    private String channel;

    private Date cdrStopTime;

    public String getFollowInfo() {
        return followInfo;
    }

    public void setFollowInfo(String followInfo) {
        this.followInfo = followInfo;
    }

    public String getFollowRemarks() {
        return followRemarks;
    }

    public void setFollowRemarks(String followRemarks) {
        this.followRemarks = followRemarks;
    }

    public String getFollowCar() {
        return followCar;
    }

    public void setFollowCar(String followCar) {
        this.followCar = followCar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDueDatePlan() {
        return dueDatePlan;
    }

    public void setDueDatePlan(Date dueDatePlan) {
        this.dueDatePlan = dueDatePlan;
    }

    public Date getDueDateActual() {
        return dueDateActual;
    }

    public void setDueDateActual(Date dueDateActual) {
        this.dueDateActual = dueDateActual;
    }

    public Integer getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(Integer taskResult) {
        this.taskResult = taskResult;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getTaskResultDesc() {
        return taskResultDesc;
    }

    public void setTaskResultDesc(String taskResultDesc) {
        this.taskResultDesc = taskResultDesc;
    }

    public String getTaskTypeDesc() {
        return taskTypeDesc;
    }

    public void setTaskTypeDesc(String taskTypeDesc) {
        this.taskTypeDesc = taskTypeDesc;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Date getFirstCallTime() {
        return firstCallTime;
    }

    public void setFirstCallTime(Date firstCallTime) {
        this.firstCallTime = firstCallTime;
    }

    public String getNextCallTime() {
        return nextCallTime;
    }

    public void setNextCallTime(String nextCallTime) {
        this.nextCallTime = nextCallTime;
    }

    public Integer getCallTimes() {
        return callTimes;
    }

    public void setCallTimes(Integer callTimes) {
        this.callTimes = callTimes;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getIsSpecialCustomer() {
        return isSpecialCustomer;
    }

    public void setIsSpecialCustomer(String isSpecialCustomer) {
        this.isSpecialCustomer = isSpecialCustomer;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public Integer getConsultingFrequency() {
        return consultingFrequency;
    }

    public void setConsultingFrequency(Integer consultingFrequency) {
        this.consultingFrequency = consultingFrequency;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubCity() {
        return subCity;
    }

    public void setSubCity(String subCity) {
        this.subCity = subCity;
    }

    public String getVehiclePurpose() {
        return vehiclePurpose;
    }

    public void setVehiclePurpose(String vehiclePurpose) {
        this.vehiclePurpose = vehiclePurpose;
    }

    public Date getTestDrivePlan() {
        return testDrivePlan;
    }

    public void setTestDrivePlan(Date testDrivePlan) {
        this.testDrivePlan = testDrivePlan;
    }

    public String getInteriorYearlyBudget() {
        return interiorYearlyBudget;
    }

    public void setInteriorYearlyBudget(String interiorYearlyBudget) {
        this.interiorYearlyBudget = interiorYearlyBudget;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getPersontitle() {
        return persontitle;
    }

    public void setPersontitle(String persontitle) {
        this.persontitle = persontitle;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Date getCdrStopTime() {
        return cdrStopTime;
    }

    public void setCdrStopTime(Date cdrStopTime) {
        this.cdrStopTime = cdrStopTime;
    }

    public Date getCustomerCarDate() {
        return customerCarDate;
    }

    public void setCustomerCarDate(Date customerCarDate) {
        this.customerCarDate = customerCarDate;
    }
}