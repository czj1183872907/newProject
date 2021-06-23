package com.chery.exeed.crm.lead.dto;

import com.chery.exeed.crm.lead.model.Customer;

import java.util.List;

public class CustomerDTO extends Customer {

    private String customerStatusDes;//客户状态
    private String genderDes; //性别
    private String nationDes; //民族
    private String credentialTypeDes; //证件类型
    private String ownerName;//所有人
    private String createdByName;//创建人
    private String modifyByName;//修改人
    private String customerLevelDes;//客户级别
    private String specialCustomerTypeDes;//特殊客户说明
    private String householdRegistrationDes;//户籍性质
    private String educationLevelDes;//文化程度
    private String revenueLevelDes;//收入水平
    private String drivingSkillDes;//驾驶技术
    private String hobbyDes;//爱号
    private String automotiveExpertiseDes;//汽车专业知识
    private String communicationDifficultyDes;//沟通难度
    private String treasureCarLevelDes;//珍惜车程度
    private String maritalStatusDes;//家庭结构
    private String ifMarriedDes;//婚姻状况
    private String occupationTypeDes;//单位类型
    private String industryDes;//行业
    private String owningCarAgeDes;//购车年龄
    private Boolean customerCarRelation; //认证车主标识
    private String followCarDes;//关注车型
    private String followInfoDes;//关注点
    private String postDes; // 岗位描述
    private String firstTimeCarPurchaseDes; // 是否首次购车描述
    private String chooseReasonDes; // 选择原因描述
    private String chooseReasonDetailDes;// 选择原因细化描述
    private String knowChannelDes;// 了解原因描述
    private String knowChannelDetailDes;// 了解原因细化描述
    private String personTitleDes;

    private List<CustomerOldCarDataDTO> oldCarData;
    private List<CustomerReferenceCarDataDTO> referenceCarData;

    public String getFollowInfoDes() {
        return followInfoDes;
    }

    public void setFollowInfoDes(String followInfoDes) {
        this.followInfoDes = followInfoDes;
    }

    public String getFollowCarDes() {
        return followCarDes;
    }

    public void setFollowCarDes(String followCarDes) {
        this.followCarDes = followCarDes;
    }

    public String getOwningCarAgeDes() {
        return owningCarAgeDes;
    }

    public void setOwningCarAgeDes(String owningCarAgeDes) {
        this.owningCarAgeDes = owningCarAgeDes;
    }

    public String getSpecialCustomerTypeDes() {
        return specialCustomerTypeDes;
    }

    public void setSpecialCustomerTypeDes(String specialCustomerTypeDes) {
        this.specialCustomerTypeDes = specialCustomerTypeDes;
    }

    public String getCustomerLevelDes() {
        return customerLevelDes;
    }

    public void setCustomerLevelDes(String customerLevelDes) {
        this.customerLevelDes = customerLevelDes;
    }

    public String getCustomerStatusDes() {
        return customerStatusDes;
    }

    public void setCustomerStatusDes(String customerStatusDes) {
        this.customerStatusDes = customerStatusDes;
    }

    public String getGenderDes() {
        return genderDes;
    }

    public void setGenderDes(String genderDes) {
        this.genderDes = genderDes;
    }

    public String getNationDes() {
        return nationDes;
    }

    public void setNationDes(String nationDes) {
        this.nationDes = nationDes;
    }

    public String getCredentialTypeDes() {
        return credentialTypeDes;
    }

    public void setCredentialTypeDes(String credentialTypeDes) {
        this.credentialTypeDes = credentialTypeDes;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public String getHobbyDes() {
        return hobbyDes;
    }

    public void setHobbyDes(String hobbyDes) {
        this.hobbyDes = hobbyDes;
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

    public String getHouseholdRegistrationDes() {
        return householdRegistrationDes;
    }

    public void setHouseholdRegistrationDes(String householdRegistrationDes) {
        this.householdRegistrationDes = householdRegistrationDes;
    }

    public String getEducationLevelDes() {
        return educationLevelDes;
    }

    public void setEducationLevelDes(String educationLevelDes) {
        this.educationLevelDes = educationLevelDes;
    }

    public String getRevenueLevelDes() {
        return revenueLevelDes;
    }

    public void setRevenueLevelDes(String revenueLevelDes) {
        this.revenueLevelDes = revenueLevelDes;
    }

    public String getDrivingSkillDes() {
        return drivingSkillDes;
    }

    public void setDrivingSkillDes(String drivingSkillDes) {
        this.drivingSkillDes = drivingSkillDes;
    }

    public String getAutomotiveExpertiseDes() {
        return automotiveExpertiseDes;
    }

    public void setAutomotiveExpertiseDes(String automotiveExpertiseDes) {
        this.automotiveExpertiseDes = automotiveExpertiseDes;
    }

    public String getCommunicationDifficultyDes() {
        return communicationDifficultyDes;
    }

    public void setCommunicationDifficultyDes(String communicationDifficultyDes) {
        this.communicationDifficultyDes = communicationDifficultyDes;
    }

    public String getTreasureCarLevelDes() {
        return treasureCarLevelDes;
    }

    public void setTreasureCarLevelDes(String treasureCarLevelDes) {
        this.treasureCarLevelDes = treasureCarLevelDes;
    }

    public String getMaritalStatusDes() {
        return maritalStatusDes;
    }

    public void setMaritalStatusDes(String maritalStatusDes) {
        this.maritalStatusDes = maritalStatusDes;
    }

    public String getIfMarriedDes() {
        return ifMarriedDes;
    }

    public void setIfMarriedDes(String ifMarriedDes) {
        this.ifMarriedDes = ifMarriedDes;
    }

    public String getOccupationTypeDes() {
        return occupationTypeDes;
    }

    public void setOccupationTypeDes(String occupationTypeDes) {
        this.occupationTypeDes = occupationTypeDes;
    }

    public String getIndustryDes() {
        return industryDes;
    }

    public void setIndustryDes(String industryDes) {
        this.industryDes = industryDes;
    }

    public Boolean getCustomerCarRelation() {
        return customerCarRelation;
    }

    public void setCustomerCarRelation(Boolean customerCarRelation) {
        this.customerCarRelation = customerCarRelation;
    }

    public String getPostDes() {
        return postDes;
    }

    public void setPostDes(String postDes) {
        this.postDes = postDes;
    }

    public String getFirstTimeCarPurchaseDes() {
        return firstTimeCarPurchaseDes;
    }

    public void setFirstTimeCarPurchaseDes(String firstTimeCarPurchaseDes) {
        this.firstTimeCarPurchaseDes = firstTimeCarPurchaseDes;
    }

    public String getChooseReasonDes() {
        return chooseReasonDes;
    }

    public void setChooseReasonDes(String chooseReasonDes) {
        this.chooseReasonDes = chooseReasonDes;
    }

    public String getChooseReasonDetailDes() {
        return chooseReasonDetailDes;
    }

    public void setChooseReasonDetailDes(String chooseReasonDetailDes) {
        this.chooseReasonDetailDes = chooseReasonDetailDes;
    }

    public String getKnowChannelDes() {
        return knowChannelDes;
    }

    public void setKnowChannelDes(String knowChannelDes) {
        this.knowChannelDes = knowChannelDes;
    }

    public String getKnowChannelDetailDes() {
        return knowChannelDetailDes;
    }

    public void setKnowChannelDetailDes(String knowChannelDetailDes) {
        this.knowChannelDetailDes = knowChannelDetailDes;
    }

    public List<CustomerOldCarDataDTO> getOldCarData() {
        return oldCarData;
    }

    public void setOldCarData(List<CustomerOldCarDataDTO> oldCarData) {
        this.oldCarData = oldCarData;
    }

    public List<CustomerReferenceCarDataDTO> getReferenceCarData() {
        return referenceCarData;
    }

    public void setReferenceCarData(List<CustomerReferenceCarDataDTO> referenceCarData) {
        this.referenceCarData = referenceCarData;
    }

    public String getPersonTitleDes() {
        return personTitleDes;
    }

    public void setPersonTitleDes(String personTitleDes) {
        this.personTitleDes = personTitleDes;
    }
}
