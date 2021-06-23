package com.chery.exeed.crm.lead.dto;

import io.swagger.models.auth.In;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class LeadResultDTO implements Serializable {

    private String leadNumber;

    private Integer leadStatus;

    private String brandName;

    private String seriesName;

    private String modelName;

    private String colorName;

    private String lastName;

    private String firstName;

    private String email;

    private String phone;

    private Integer rating;

    private String level;
    private Date testDrivePlan;

    private Integer budget;

    private String budgetName;

    private String rejectReason;

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

    private String createdByName;

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

    private Integer haveTask;

    private String specialCareComments;

    private Integer id;

    private String leadType;

    private String province;

    private String city;

    private String subCity;

    private String address;

    private String gender;

    private Integer age;

    private String description;

    private String purchaseTime;

    private String custName;

    private String dealerName;

    private String ratingName;

    private String ownerName;

    private String status;

    private Integer overDate;

    private Integer custId;

    private Integer channelId;

    private Integer subChannelId;

    private String channelName;

    private String modifyByName;

    private String subChannelName;

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

    private String campaignId;

    private String purchaseTimeCode;
    //预计跟进时间
    private Date followPlan;

    private Date followDate;

    //战败类别
    private String loseType;

    //客户反馈
    private String feedback;

    //跟进次数
    private Integer followupTimes;
    private Integer arrivalTimes;
    private Integer predictTimes;

    //渠道来源次数
    private Integer channelResourceTimes;

    //分配的销售顾问
    private String ownerDesc;

    //申请战败时间
    private Date defeatApplyTime;

    private Long toExpiredTime;

    private Date sendTime;

    private String followCar;
    private String followCarDes;

    private String followInfo;
    private String followInfoDes;
    private String followRemarks;

    private String customerStatus;

    private String theKeyInformation;

    private List<LeadChannelDTO> channelList;

    private String defeatApprovalDesc;

    private Integer canTimeout;

    private Integer totals; //总数

    private Integer isItFloatingRed;//是否飘红

    private Date updateRecordTime;//更新时间

    private Date operateTime;//操作时间

    private Integer isItOneHourDistribution;//是否一小时分配

    private String dealerSendTime;//经销商下发时间

    private String firstFollowupTime;//首次跟进时间

    private Integer leadHeatingCount;//线索加热次数

    private Integer isRedOrBlack;//是红是黑（1红0黑）

    private String loseTypeName;//战败类别名称

    private Integer flag;//标识

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getDealerSendTime() {
        return dealerSendTime;
    }

    public void setDealerSendTime(String dealerSendTime) {
        this.dealerSendTime = dealerSendTime;
    }

    public String getFirstFollowupTime() {
        return firstFollowupTime;
    }

    public void setFirstFollowupTime(String firstFollowupTime) {
        this.firstFollowupTime = firstFollowupTime;
    }

    public Integer getTotals() {
        return totals;
    }

    public void setTotals(Integer totals) {
        this.totals = totals;
    }

    public Integer getCanTimeout() {
        return canTimeout;
    }

    public void setCanTimeout(Integer canTimeout) {
        this.canTimeout = canTimeout;
    }

    public String getTheKeyInformation() {
        return theKeyInformation;
    }

    public void setTheKeyInformation(String theKeyInformation) {
        this.theKeyInformation = theKeyInformation;
    }

    public Integer getArrivalTimes() {
        return arrivalTimes;
    }

    public void setArrivalTimes(Integer arrivalTimes) {
        this.arrivalTimes = arrivalTimes;
    }

    public Integer getPredictTimes() {
        return predictTimes;
    }

    public void setPredictTimes(Integer predictTimes) {
        this.predictTimes = predictTimes;
    }

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

    public String getFollowCar() {
        return followCar;
    }

    public void setFollowCar(String followCar) {
        this.followCar = followCar;
    }

    public void setFollowCarDes(String followCarDes) {
        this.followCarDes = followCarDes;
    }
    public String getDefeatApprovalDesc() {
        return defeatApprovalDesc;
    }

    public void setDefeatApprovalDesc(String defeatApprovalDesc) {
        this.defeatApprovalDesc = defeatApprovalDesc;
    }

    public String getLeadNumber() {
        return leadNumber;
    }

    public void setLeadNumber(String leadNumber) {
        this.leadNumber = leadNumber;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        this.maritalStatus = maritalStatus;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getRevenueLevel() {
        return revenueLevel;
    }

    public void setRevenueLevel(String revenueLevel) {
        this.revenueLevel = revenueLevel;
    }

    public String getHouseholdRegistration() {
        return householdRegistration;
    }

    public void setHouseholdRegistration(String householdRegistration) {
        this.householdRegistration = householdRegistration;
    }

    public String getConsumptionCharacteristics() {
        return consumptionCharacteristics;
    }

    public void setConsumptionCharacteristics(String consumptionCharacteristics) {
        this.consumptionCharacteristics = consumptionCharacteristics;
    }

    public String getPurchaeFrequency() {
        return purchaeFrequency;
    }

    public void setPurchaeFrequency(String purchaeFrequency) {
        this.purchaeFrequency = purchaeFrequency;
    }

    public String getInteriorYearlyBudget() {
        return interiorYearlyBudget;
    }

    public void setInteriorYearlyBudget(String interiorYearlyBudget) {
        this.interiorYearlyBudget = interiorYearlyBudget;
    }

    public String getDrivingSkill() {
        return drivingSkill;
    }

    public void setDrivingSkill(String drivingSkill) {
        this.drivingSkill = drivingSkill;
    }

    public String getAutomotiveExpertise() {
        return automotiveExpertise;
    }

    public void setAutomotiveExpertise(String automotiveExpertise) {
        this.automotiveExpertise = automotiveExpertise;
    }

    public String getCommunicationDifficulty() {
        return communicationDifficulty;
    }

    public void setCommunicationDifficulty(String communicationDifficulty) {
        this.communicationDifficulty = communicationDifficulty;
    }

    public String getTreasureCarLevel() {
        return treasureCarLevel;
    }

    public void setTreasureCarLevel(String treasureCarLevel) {
        this.treasureCarLevel = treasureCarLevel;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
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

    public String getSpecialCustomerType() {
        return specialCustomerType;
    }

    public void setSpecialCustomerType(String specialCustomerType) {
        this.specialCustomerType = specialCustomerType;
    }

    public String getSpecialCareComments() {
        return specialCareComments;
    }

    public void setSpecialCareComments(String specialCareComments) {
        this.specialCareComments = specialCareComments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLeadType() {
        return leadType;
    }

    public void setLeadType(String leadType) {
        this.leadType = leadType;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getSubChannelId() {
        return subChannelId;
    }

    public void setSubChannelId(Integer subChannelId) {
        this.subChannelId = subChannelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getSubChannelName() {
        return subChannelName;
    }

    public void setSubChannelName(String subChannelName) {
        this.subChannelName = subChannelName;
    }

    public String getIfMarried() {
        return ifMarried;
    }

    public void setIfMarried(String ifMarried) {
        this.ifMarried = ifMarried;
    }

    public String getFamilySize() {
        return familySize;
    }

    public void setFamilySize(String familySize) {
        this.familySize = familySize;
    }

    public String getOwningCarAge() {
        return owningCarAge;
    }

    public void setOwningCarAge(String owningCarAge) {
        this.owningCarAge = owningCarAge;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getVehiclePurpose() {
        return vehiclePurpose;
    }

    public void setVehiclePurpose(String vehiclePurpose) {
        this.vehiclePurpose = vehiclePurpose;
    }

    public String getRecomender() {
        return recomender;
    }

    public void setRecomender(String recomender) {
        this.recomender = recomender;
    }

    public String getRecomenderPhone() {
        return recomenderPhone;
    }

    public void setRecomenderPhone(String recomenderPhone) {
        this.recomenderPhone = recomenderPhone;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getModifyByName() {
        return modifyByName;
    }

    public void setModifyByName(String modifyByName) {
        this.modifyByName = modifyByName;
    }

    public String getOccupationType() {
        return occupationType;
    }

    public void setOccupationType(String occupationType) {
        this.occupationType = occupationType;
    }

    public String getOccupationPhone() {
        return occupationPhone;
    }

    public void setOccupationPhone(String occupationPhone) {
        this.occupationPhone = occupationPhone;
    }

    public String getCallReason() {
        return callReason;
    }

    public void setCallReason(String callReason) {
        this.callReason = callReason;
    }

    public String getWechatNumber() {
        return wechatNumber;
    }

    public void setWechatNumber(String wechatNumber) {
        this.wechatNumber = wechatNumber;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public Date getFollowPlan() {
        return followPlan;
    }

    public void setFollowPlan(Date followPlan) {
        this.followPlan = followPlan;
    }

    public Date getFollowDate() {
        return followDate;
    }

    public void setFollowDate(Date followDate) {
        this.followDate = followDate;
    }

    public String getLoseType() {
        return loseType;
    }

    public void setLoseType(String loseType) {
        this.loseType = loseType;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Integer getFollowupTimes() {
        return followupTimes;
    }

    public void setFollowupTimes(Integer followupTimes) {
        this.followupTimes = followupTimes;
    }

    public Integer getChannelResourceTimes() {
        return channelResourceTimes;
    }

    public void setChannelResourceTimes(Integer channelResourceTimes) {
        this.channelResourceTimes = channelResourceTimes;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getPurchaseTimeCode() {
        return purchaseTimeCode;
    }

    public void setPurchaseTimeCode(String purchaseTimeCode) {
        this.purchaseTimeCode = purchaseTimeCode;
    }

    public String getOwnerDesc() {
        return ownerDesc;
    }

    public void setOwnerDesc(String ownerDesc) {
        this.ownerDesc = ownerDesc;
    }

    public List<LeadChannelDTO> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<LeadChannelDTO> channelList) {
        this.channelList = channelList;
    }

    public Date getDefeatApplyTime() {
        return defeatApplyTime;
    }

    public void setDefeatApplyTime(Date defeatApplyTime) {
        this.defeatApplyTime = defeatApplyTime;
    }

    public Integer getHaveTask() {
        return haveTask;
    }

    public void setHaveTask(Integer haveTask) {
        this.haveTask = haveTask;
    }

    public Long getToExpiredTime() {
        if( this.getFollowPlan() != null){
            long plan = this.getFollowPlan().getTime();
            long now = System.currentTimeMillis();
            return (plan-now);
        }
        return null;
    }

    public void setToExpiredTime(Long toExpiredTime) {
        this.toExpiredTime = toExpiredTime;
    }

    public Integer getOverDate() {
        return overDate;
    }

    public void setOverDate(Integer overDate) {
        this.overDate = overDate;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public Integer getIsItFloatingRed() {
        return isItFloatingRed;
    }

    public void setIsItFloatingRed(Integer isItFloatingRed) {
        this.isItFloatingRed = isItFloatingRed;
    }

    public Date getUpdateRecordTime() {
        return updateRecordTime;
    }

    public void setUpdateRecordTime(Date updateRecordTime) {
        this.updateRecordTime = updateRecordTime;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getIsItOneHourDistribution() {
        return isItOneHourDistribution;
    }

    public void setIsItOneHourDistribution(Integer isItOneHourDistribution) {
        this.isItOneHourDistribution = isItOneHourDistribution;
    }

    public Integer getLeadHeatingCount() {
        return leadHeatingCount;
    }

    public void setLeadHeatingCount(Integer leadHeatingCount) {
        this.leadHeatingCount = leadHeatingCount;
    }

    public Integer getIsRedOrBlack() {
        return isRedOrBlack;
    }

    public void setIsRedOrBlack(Integer isRedOrBlack) {
        this.isRedOrBlack = isRedOrBlack;
    }

    public String getLoseTypeName() {
        return loseTypeName;
    }

    public void setLoseTypeName(String loseTypeName) {
        this.loseTypeName = loseTypeName;
    }
}