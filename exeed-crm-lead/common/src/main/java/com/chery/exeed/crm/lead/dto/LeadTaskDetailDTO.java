package com.chery.exeed.crm.lead.dto;

import java.util.Date;

public class LeadTaskDetailDTO{
    private Long id;

    private String phone;

    private Integer leadId;

    private Integer taskType;

    private Integer owner;

    private String ownerName;

    private Integer status;

    private Date dueDatePlan;

    private Date dueDateActual;

    private Integer taskResult;
    private String statusDesc;
    private String taskResultDesc;
    private String taskTypeDesc;
    private Date modifyTime;

    private String memo;

    /**购车用途*/
    private String vehiclePurpose;

    private Date firstCallTime;

    private String nextCallTime;

    private Integer callTimes;

    private String leadNumber;

    private Integer leadType;

    private Integer leadStatus;

    private String brandName;

    private String seriesName;

    private String modelName;

    private String colorName;

    private String lastName;

    private String firstName;

    private String email;

    private Integer rating;

    private String level;

    private Date testDrivePlan;

    private Integer budget;

    private String budgetName;

    private Integer consultingFrequency;

    private String wechatOpenid;

    private Date closeTime;

    private String failureReason;

    private String profileUrl;

    private String dealer;

    private String dealerOrderManager;

    private String productExperienceManager;

    private String account;

    private Date customerCarDate;

    private Integer createdBy;

    private Integer modifyBy;

    private Date modifyDate;

    private Integer personDonotcall;

    private Integer maritalStatus;

    private String hobby;

    private Integer educationLevel;

    private Integer revenueLevel;

    private Integer householdRegistration;

    private String consumptionCharacteristics;

    private Integer purchaeFrequency;

    private Integer interiorYearlyBudget;

    private Integer drivingSkill;

    private Integer automotiveExpertise;

    private Integer communicationDifficulty;

    private Integer treasureCarLevel;

    private Integer vehicleNo;

    private String customerCharacteristicsDes;

    private String companyName;

    private Integer industry;

    private String persontitle;

    private Integer isSpecialCustomer;

    private Integer specialCustomerType;

    private String specialCareComments;

    private String LeadType;

    private String province;

    private String city;

    private String subCity;

    private String address;

    private String gender;

    private Integer age;

    private Integer assignedConsultant;

    private String description;

    private String purchaseTime;

    private String custName;

    private String dealerName;

    private String ratingName;

    private Integer custId;

    private String leadStatusDesc;

    private String campaignId;

    private String followCar;
    private String followCarDes;
    private Date cdrStopTime;

    private String followInfo;
    private String followInfoDes;
    private String followRemarks;

    public String getFollowInfo() {
        return followInfo;
    }

    public void setFollowInfo(String followInfo) {
        this.followInfo = followInfo;
    }

    public String getFollowInfoDes() {
        return followInfoDes;
    }

    public void setFollowInfoDes(String followInfoDes) {
        this.followInfoDes = followInfoDes;
    }

    public String getFollowRemarks() {
        return followRemarks;
    }

    public void setFollowRemarks(String followRemarks) {
        this.followRemarks = followRemarks;
    }

    public String getFollowCarDes() {
        return followCarDes;
    }

    public void setFollowCarDes(String followCarDes) {
        this.followCarDes = followCarDes;
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

    public String getLeadNumber() {
        return leadNumber;
    }

    public void setLeadNumber(String leadNumber) {
        this.leadNumber = leadNumber;
    }

    public Integer getLeadType() {
        return leadType;
    }

    public void setLeadType(String leadType) {
        LeadType = leadType;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAssignedConsultant() {
        return assignedConsultant;
    }

    public void setAssignedConsultant(Integer assignedConsultant) {
        this.assignedConsultant = assignedConsultant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getRatingName() {
        return ratingName;
    }

    public void setRatingName(String ratingName) {
        this.ratingName = ratingName;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public void setLeadType(Integer leadType) {
        this.leadType = leadType;
    }

    public Integer getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(Integer leadStatus) {
        this.leadStatus = leadStatus;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Date getTestDrivePlan() {
        return testDrivePlan;
    }

    public void setTestDrivePlan(Date testDrivePlan) {
        this.testDrivePlan = testDrivePlan;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public Integer getConsultingFrequency() {
        return consultingFrequency;
    }

    public void setConsultingFrequency(Integer consultingFrequency) {
        this.consultingFrequency = consultingFrequency;
    }

    public String getWechatOpenid() {
        return wechatOpenid;
    }

    public void setWechatOpenid(String wechatOpenid) {
        this.wechatOpenid = wechatOpenid;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getDealerOrderManager() {
        return dealerOrderManager;
    }

    public void setDealerOrderManager(String dealerOrderManager) {
        this.dealerOrderManager = dealerOrderManager;
    }

    public String getProductExperienceManager() {
        return productExperienceManager;
    }

    public void setProductExperienceManager(String productExperienceManager) {
        this.productExperienceManager = productExperienceManager;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getCustomerCarDate() {
        return customerCarDate;
    }

    public void setCustomerCarDate(Date customerCarDate) {
        this.customerCarDate = customerCarDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(Integer modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getPersonDonotcall() {
        return personDonotcall;
    }

    public void setPersonDonotcall(Integer personDonotcall) {
        this.personDonotcall = personDonotcall;
    }

    public Integer getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Integer maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public Integer getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(Integer educationLevel) {
        this.educationLevel = educationLevel;
    }

    public Integer getRevenueLevel() {
        return revenueLevel;
    }

    public void setRevenueLevel(Integer revenueLevel) {
        this.revenueLevel = revenueLevel;
    }

    public Integer getHouseholdRegistration() {
        return householdRegistration;
    }

    public void setHouseholdRegistration(Integer householdRegistration) {
        this.householdRegistration = householdRegistration;
    }

    public String getConsumptionCharacteristics() {
        return consumptionCharacteristics;
    }

    public void setConsumptionCharacteristics(String consumptionCharacteristics) {
        this.consumptionCharacteristics = consumptionCharacteristics;
    }

    public Integer getPurchaeFrequency() {
        return purchaeFrequency;
    }

    public void setPurchaeFrequency(Integer purchaeFrequency) {
        this.purchaeFrequency = purchaeFrequency;
    }

    public Integer getInteriorYearlyBudget() {
        return interiorYearlyBudget;
    }

    public void setInteriorYearlyBudget(Integer interiorYearlyBudget) {
        this.interiorYearlyBudget = interiorYearlyBudget;
    }

    public Integer getDrivingSkill() {
        return drivingSkill;
    }

    public void setDrivingSkill(Integer drivingSkill) {
        this.drivingSkill = drivingSkill;
    }

    public Integer getAutomotiveExpertise() {
        return automotiveExpertise;
    }

    public void setAutomotiveExpertise(Integer automotiveExpertise) {
        this.automotiveExpertise = automotiveExpertise;
    }

    public Integer getCommunicationDifficulty() {
        return communicationDifficulty;
    }

    public void setCommunicationDifficulty(Integer communicationDifficulty) {
        this.communicationDifficulty = communicationDifficulty;
    }

    public Integer getTreasureCarLevel() {
        return treasureCarLevel;
    }

    public void setTreasureCarLevel(Integer treasureCarLevel) {
        this.treasureCarLevel = treasureCarLevel;
    }

    public Integer getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(Integer vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getCustomerCharacteristicsDes() {
        return customerCharacteristicsDes;
    }

    public void setCustomerCharacteristicsDes(String customerCharacteristicsDes) {
        this.customerCharacteristicsDes = customerCharacteristicsDes;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getIndustry() {
        return industry;
    }

    public void setIndustry(Integer industry) {
        this.industry = industry;
    }

    public String getPersontitle() {
        return persontitle;
    }

    public void setPersontitle(String persontitle) {
        this.persontitle = persontitle;
    }

    public Integer getIsSpecialCustomer() {
        return isSpecialCustomer;
    }

    public void setIsSpecialCustomer(Integer isSpecialCustomer) {
        this.isSpecialCustomer = isSpecialCustomer;
    }

    public Integer getSpecialCustomerType() {
        return specialCustomerType;
    }

    public void setSpecialCustomerType(Integer specialCustomerType) {
        this.specialCustomerType = specialCustomerType;
    }

    public String getSpecialCareComments() {
        return specialCareComments;
    }

    public void setSpecialCareComments(String specialCareComments) {
        this.specialCareComments = specialCareComments;
    }

    public String getLeadStatusDesc() {
        return leadStatusDesc;
    }

    public void setLeadStatusDesc(String leadStatusDesc) {
        this.leadStatusDesc = leadStatusDesc;
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

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getVehiclePurpose() {
        return vehiclePurpose;
    }

    public void setVehiclePurpose(String vehiclePurpose) {
        this.vehiclePurpose = vehiclePurpose;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCdrStopTime() {
        return cdrStopTime;
    }

    public void setCdrStopTime(Date cdrStopTime) {
        this.cdrStopTime = cdrStopTime;
    }
}