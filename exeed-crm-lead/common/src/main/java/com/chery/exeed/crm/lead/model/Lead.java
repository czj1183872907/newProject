package com.chery.exeed.crm.lead.model;

import java.util.Date;

public class Lead {
    private Integer id;

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

    private String phone;

    private String province;

    private String city;

    private String subCity;

    private String address;

    private Integer gender;

    private Integer age;

    private String description;

    private String purchaseTime;

    private Integer rating;

    private String level;

    private Date testDrivePlan;

    private String budget;

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

    private Integer owner;

    private Integer createdBy;

    private Integer modifyBy;

    private Date modifyDate;

    private Integer personDonotcall;

    private String maritalStatus;

    private String hobby;

    private String educationLevel;

    private String revenueLevel;

    private String householdRegistration;

    private String consumptionCharacteristics;

    private String purchaeFrequency;

    private String interiorYearlyBudget;

    private String drivingSkill;

    private String automotiveExpertise;

    private String communicationDifficulty;

    private String treasureCarLevel;

    private String vehicleNo;

    private String customerCharacteristicsDes;

    private String companyName;

    private String industry;

    private String persontitle;

    private Integer isSpecialCustomer;

    private String specialCustomerType;

    private String specialCareComments;

    private Integer custId;

    private String ifMarried;

    private String familySize;

    private String owningCarAge;

    private String officePhone;

    private String vehiclePurpose;

    private String recomender;

    private String recomenderPhone;

    private String occupationType;

    private String occupationPhone;

    private String callReason;

    private String wechatNumber;

    private String rejectReason;

    private String campaignId;

    private String resourceId;

    private String followCar;

    private String rejectReasonType;

    private Date collectTime;

    private Date followTime;

    private Integer followBy;

    private String followInfo;

    private String followRemarks;

    private Date sendTime;

    private String categoryName;

    private String modelCode;

    private String productCode;

    private Integer leadHeatingCount;//线索热度次数

    private String vinNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLeadNumber() {
        return leadNumber;
    }

    public void setLeadNumber(String leadNumber) {
        this.leadNumber = leadNumber == null ? null : leadNumber.trim();
    }

    public Integer getLeadType() {
        return leadType;
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
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName == null ? null : seriesName.trim();
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName == null ? null : colorName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName == null ? null : lastName.trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName == null ? null : firstName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getSubCity() {
        return subCity;
    }

    public void setSubCity(String subCity) {
        this.subCity = subCity == null ? null : subCity.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime == null ? null : purchaseTime.trim();
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
        this.level = level == null ? null : level.trim();
    }

    public Date getTestDrivePlan() {
        return testDrivePlan;
    }

    public void setTestDrivePlan(Date testDrivePlan) {
        this.testDrivePlan = testDrivePlan;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget == null ? null : budget.trim();
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
        this.wechatOpenid = wechatOpenid == null ? null : wechatOpenid.trim();
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
        this.failureReason = failureReason == null ? null : failureReason.trim();
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl == null ? null : profileUrl.trim();
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer == null ? null : dealer.trim();
    }

    public String getDealerOrderManager() {
        return dealerOrderManager;
    }

    public void setDealerOrderManager(String dealerOrderManager) {
        this.dealerOrderManager = dealerOrderManager == null ? null : dealerOrderManager.trim();
    }

    public String getProductExperienceManager() {
        return productExperienceManager;
    }

    public void setProductExperienceManager(String productExperienceManager) {
        this.productExperienceManager = productExperienceManager == null ? null : productExperienceManager.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public Date getCustomerCarDate() {
        return customerCarDate;
    }

    public void setCustomerCarDate(Date customerCarDate) {
        this.customerCarDate = customerCarDate;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
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

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus == null ? null : maritalStatus.trim();
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby == null ? null : hobby.trim();
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel == null ? null : educationLevel.trim();
    }

    public String getRevenueLevel() {
        return revenueLevel;
    }

    public void setRevenueLevel(String revenueLevel) {
        this.revenueLevel = revenueLevel == null ? null : revenueLevel.trim();
    }

    public String getHouseholdRegistration() {
        return householdRegistration;
    }

    public void setHouseholdRegistration(String householdRegistration) {
        this.householdRegistration = householdRegistration == null ? null : householdRegistration.trim();
    }

    public String getConsumptionCharacteristics() {
        return consumptionCharacteristics;
    }

    public void setConsumptionCharacteristics(String consumptionCharacteristics) {
        this.consumptionCharacteristics = consumptionCharacteristics == null ? null : consumptionCharacteristics.trim();
    }

    public String getPurchaeFrequency() {
        return purchaeFrequency;
    }

    public void setPurchaeFrequency(String purchaeFrequency) {
        this.purchaeFrequency = purchaeFrequency == null ? null : purchaeFrequency.trim();
    }

    public String getInteriorYearlyBudget() {
        return interiorYearlyBudget;
    }

    public void setInteriorYearlyBudget(String interiorYearlyBudget) {
        this.interiorYearlyBudget = interiorYearlyBudget == null ? null : interiorYearlyBudget.trim();
    }

    public String getDrivingSkill() {
        return drivingSkill;
    }

    public void setDrivingSkill(String drivingSkill) {
        this.drivingSkill = drivingSkill == null ? null : drivingSkill.trim();
    }

    public String getAutomotiveExpertise() {
        return automotiveExpertise;
    }

    public void setAutomotiveExpertise(String automotiveExpertise) {
        this.automotiveExpertise = automotiveExpertise == null ? null : automotiveExpertise.trim();
    }

    public String getCommunicationDifficulty() {
        return communicationDifficulty;
    }

    public void setCommunicationDifficulty(String communicationDifficulty) {
        this.communicationDifficulty = communicationDifficulty == null ? null : communicationDifficulty.trim();
    }

    public String getTreasureCarLevel() {
        return treasureCarLevel;
    }

    public void setTreasureCarLevel(String treasureCarLevel) {
        this.treasureCarLevel = treasureCarLevel == null ? null : treasureCarLevel.trim();
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo == null ? null : vehicleNo.trim();
    }

    public String getCustomerCharacteristicsDes() {
        return customerCharacteristicsDes;
    }

    public void setCustomerCharacteristicsDes(String customerCharacteristicsDes) {
        this.customerCharacteristicsDes = customerCharacteristicsDes == null ? null : customerCharacteristicsDes.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry == null ? null : industry.trim();
    }

    public String getPersontitle() {
        return persontitle;
    }

    public void setPersontitle(String persontitle) {
        this.persontitle = persontitle == null ? null : persontitle.trim();
    }

    public Integer getIsSpecialCustomer() {
        return isSpecialCustomer;
    }

    public void setIsSpecialCustomer(Integer isSpecialCustomer) {
        this.isSpecialCustomer = isSpecialCustomer;
    }

    public String getSpecialCustomerType() {
        return specialCustomerType;
    }

    public void setSpecialCustomerType(String specialCustomerType) {
        this.specialCustomerType = specialCustomerType == null ? null : specialCustomerType.trim();
    }

    public String getSpecialCareComments() {
        return specialCareComments;
    }

    public void setSpecialCareComments(String specialCareComments) {
        this.specialCareComments = specialCareComments == null ? null : specialCareComments.trim();
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getIfMarried() {
        return ifMarried;
    }

    public void setIfMarried(String ifMarried) {
        this.ifMarried = ifMarried == null ? null : ifMarried.trim();
    }

    public String getFamilySize() {
        return familySize;
    }

    public void setFamilySize(String familySize) {
        this.familySize = familySize == null ? null : familySize.trim();
    }

    public String getOwningCarAge() {
        return owningCarAge;
    }

    public void setOwningCarAge(String owningCarAge) {
        this.owningCarAge = owningCarAge == null ? null : owningCarAge.trim();
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone == null ? null : officePhone.trim();
    }

    public String getVehiclePurpose() {
        return vehiclePurpose;
    }

    public void setVehiclePurpose(String vehiclePurpose) {
        this.vehiclePurpose = vehiclePurpose == null ? null : vehiclePurpose.trim();
    }

    public String getRecomender() {
        return recomender;
    }

    public void setRecomender(String recomender) {
        this.recomender = recomender == null ? null : recomender.trim();
    }

    public String getRecomenderPhone() {
        return recomenderPhone;
    }

    public void setRecomenderPhone(String recomenderPhone) {
        this.recomenderPhone = recomenderPhone == null ? null : recomenderPhone.trim();
    }

    public String getOccupationType() {
        return occupationType;
    }

    public void setOccupationType(String occupationType) {
        this.occupationType = occupationType == null ? null : occupationType.trim();
    }

    public String getOccupationPhone() {
        return occupationPhone;
    }

    public void setOccupationPhone(String occupationPhone) {
        this.occupationPhone = occupationPhone == null ? null : occupationPhone.trim();
    }

    public String getCallReason() {
        return callReason;
    }

    public void setCallReason(String callReason) {
        this.callReason = callReason == null ? null : callReason.trim();
    }

    public String getWechatNumber() {
        return wechatNumber;
    }

    public void setWechatNumber(String wechatNumber) {
        this.wechatNumber = wechatNumber == null ? null : wechatNumber.trim();
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason == null ? null : rejectReason.trim();
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId == null ? null : campaignId.trim();
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId == null ? null : resourceId.trim();
    }

    public String getFollowCar() {
        return followCar;
    }

    public void setFollowCar(String followCar) {
        this.followCar = followCar == null ? null : followCar.trim();
    }

    public String getRejectReasonType() {
        return rejectReasonType;
    }

    public void setRejectReasonType(String rejectReasonType) {
        this.rejectReasonType = rejectReasonType == null ? null : rejectReasonType.trim();
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public Date getFollowTime() {
        return followTime;
    }

    public void setFollowTime(Date followTime) {
        this.followTime = followTime;
    }

    public Integer getFollowBy() {
        return followBy;
    }

    public void setFollowBy(Integer followBy) {
        this.followBy = followBy;
    }

    public String getFollowInfo() {
        return followInfo;
    }

    public void setFollowInfo(String followInfo) {
        this.followInfo = followInfo == null ? null : followInfo.trim();
    }

    public String getFollowRemarks() {
        return followRemarks;
    }

    public void setFollowRemarks(String followRemarks) {
        this.followRemarks = followRemarks == null ? null : followRemarks.trim();
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode == null ? null : modelCode.trim();
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    public Integer getLeadHeatingCount() {
        return leadHeatingCount;
    }

    public void setLeadHeatingCount(Integer leadHeatingCount) {
        this.leadHeatingCount = leadHeatingCount;
    }

    public String getVinNo() {
        return vinNo;
    }

    public void setVinNo(String vinNo) {
        this.vinNo = vinNo;
    }
}