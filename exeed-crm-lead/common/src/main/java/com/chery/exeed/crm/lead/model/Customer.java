package com.chery.exeed.crm.lead.model;

import java.util.Date;

public class Customer {
    private Integer id;

    private String dmsCode;

    private String lastName;

    private String firstName;

    private String wechatOpenid;

    private Integer customerStatus;

    private Integer gender;

    private Integer nation;

    private Date personBirthdate;

    private String headImage;

    private Integer credentialType;

    private String credentialNumber;

    private String driverLicenseNo;

    private Integer customerLevel;

    private String phoneTwo;

    private String phone;

    private String personEmail;

    private Integer province;

    private String provinceName;

    private Integer city;

    private String cityName;

    private Integer subcity;

    private String subcityName;

    private String address;

    private Integer age;

    private Integer owner;

    private Integer createdBy;

    private Date customerCarDate;

    private Integer modifyBy;

    private Date modifyDate;

    private Date sendModifyTime;

    private String description;

    private Integer personDonotcall;

    private Integer maritalStatus;

    private String hobby;

    private Integer educationLevel;

    private Integer revenueLevel;

    private Integer householdRegistration;

    private String consumptionCharacteristics;

    private Integer purchaeFrequency;

    private String interiorYearlyBudget;

    private Integer drivingSkill;

    private Integer automotiveExpertise;

    private Integer communicationDifficulty;

    private Integer treasureCarLevel;

    private Integer vehicleNo;

    private String customerCharacteristicsDes;

    private String companyName;

    private String industry;

    private String personTitle;

    private Integer isSpecialCustomer;

    private Integer specialCustomerType;

    private String specialCareComments;

    private Integer leadFrequency;

    private Integer owningCarAge;

    private String recomender;

    private String recomenderPhone;

    private Integer ifMarried;

    private String occupationPhone;

    private Integer occupationType;

    private Integer customerType;

    private String contact;

    private String contactPhone;

    private Integer consultingFrequency;

    private String phoneThree;

    private String followCar;

    private String followInfo;

    private String followRemarks;

    private Integer post;

    private Integer firstTimeCarPurchase;

    private String chooseReason;

    private String chooseReasonDetail;

    private String chooseReasonRemarks;

    private String knowChannel;

    private String knowChannelDetail;

    private String knowChannelRemarks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDmsCode() {
        return dmsCode;
    }

    public void setDmsCode(String dmsCode) {
        this.dmsCode = dmsCode == null ? null : dmsCode.trim();
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

    public String getWechatOpenid() {
        return wechatOpenid;
    }

    public void setWechatOpenid(String wechatOpenid) {
        this.wechatOpenid = wechatOpenid == null ? null : wechatOpenid.trim();
    }

    public Integer getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(Integer customerStatus) {
        this.customerStatus = customerStatus;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getNation() {
        return nation;
    }

    public void setNation(Integer nation) {
        this.nation = nation;
    }

    public Date getPersonBirthdate() {
        return personBirthdate;
    }

    public void setPersonBirthdate(Date personBirthdate) {
        this.personBirthdate = personBirthdate;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage == null ? null : headImage.trim();
    }

    public Integer getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(Integer credentialType) {
        this.credentialType = credentialType;
    }

    public String getCredentialNumber() {
        return credentialNumber;
    }

    public void setCredentialNumber(String credentialNumber) {
        this.credentialNumber = credentialNumber == null ? null : credentialNumber.trim();
    }

    public String getDriverLicenseNo() {
        return driverLicenseNo;
    }

    public void setDriverLicenseNo(String driverLicenseNo) {
        this.driverLicenseNo = driverLicenseNo == null ? null : driverLicenseNo.trim();
    }

    public Integer getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(Integer customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getPhoneTwo() {
        return phoneTwo;
    }

    public void setPhoneTwo(String phoneTwo) {
        this.phoneTwo = phoneTwo == null ? null : phoneTwo.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail == null ? null : personEmail.trim();
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName == null ? null : provinceName.trim();
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public Integer getSubcity() {
        return subcity;
    }

    public void setSubcity(Integer subcity) {
        this.subcity = subcity;
    }

    public String getSubcityName() {
        return subcityName;
    }

    public void setSubcityName(String subcityName) {
        this.subcityName = subcityName == null ? null : subcityName.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public Date getCustomerCarDate() {
        return customerCarDate;
    }

    public void setCustomerCarDate(Date customerCarDate) {
        this.customerCarDate = customerCarDate;
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

    public Date getSendModifyTime() {
        return sendModifyTime;
    }

    public void setSendModifyTime(Date sendModifyTime) {
        this.sendModifyTime = sendModifyTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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
        this.hobby = hobby == null ? null : hobby.trim();
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
        this.consumptionCharacteristics = consumptionCharacteristics == null ? null : consumptionCharacteristics.trim();
    }

    public Integer getPurchaeFrequency() {
        return purchaeFrequency;
    }

    public void setPurchaeFrequency(Integer purchaeFrequency) {
        this.purchaeFrequency = purchaeFrequency;
    }

    public String getInteriorYearlyBudget() {
        return interiorYearlyBudget;
    }

    public void setInteriorYearlyBudget(String interiorYearlyBudget) {
        this.interiorYearlyBudget = interiorYearlyBudget == null ? null : interiorYearlyBudget.trim();
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

    public String getPersonTitle() {
        return personTitle;
    }

    public void setPersonTitle(String personTitle) {
        this.personTitle = personTitle == null ? null : personTitle.trim();
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
        this.specialCareComments = specialCareComments == null ? null : specialCareComments.trim();
    }

    public Integer getLeadFrequency() {
        return leadFrequency;
    }

    public void setLeadFrequency(Integer leadFrequency) {
        this.leadFrequency = leadFrequency;
    }

    public Integer getOwningCarAge() {
        return owningCarAge;
    }

    public void setOwningCarAge(Integer owningCarAge) {
        this.owningCarAge = owningCarAge;
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

    public Integer getIfMarried() {
        return ifMarried;
    }

    public void setIfMarried(Integer ifMarried) {
        this.ifMarried = ifMarried;
    }

    public String getOccupationPhone() {
        return occupationPhone;
    }

    public void setOccupationPhone(String occupationPhone) {
        this.occupationPhone = occupationPhone == null ? null : occupationPhone.trim();
    }

    public Integer getOccupationType() {
        return occupationType;
    }

    public void setOccupationType(Integer occupationType) {
        this.occupationType = occupationType;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    public Integer getConsultingFrequency() {
        return consultingFrequency;
    }

    public void setConsultingFrequency(Integer consultingFrequency) {
        this.consultingFrequency = consultingFrequency;
    }

    public String getPhoneThree() {
        return phoneThree;
    }

    public void setPhoneThree(String phoneThree) {
        this.phoneThree = phoneThree == null ? null : phoneThree.trim();
    }

    public String getFollowCar() {
        return followCar;
    }

    public void setFollowCar(String followCar) {
        this.followCar = followCar == null ? null : followCar.trim();
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

    public Integer getPost() {
        return post;
    }

    public void setPost(Integer post) {
        this.post = post;
    }

    public Integer getFirstTimeCarPurchase() {
        return firstTimeCarPurchase;
    }

    public void setFirstTimeCarPurchase(Integer firstTimeCarPurchase) {
        this.firstTimeCarPurchase = firstTimeCarPurchase;
    }

    public String getChooseReason() {
        return chooseReason;
    }

    public void setChooseReason(String chooseReason) {
        this.chooseReason = chooseReason == null ? null : chooseReason.trim();
    }

    public String getChooseReasonDetail() {
        return chooseReasonDetail;
    }

    public void setChooseReasonDetail(String chooseReasonDetail) {
        this.chooseReasonDetail = chooseReasonDetail == null ? null : chooseReasonDetail.trim();
    }

    public String getChooseReasonRemarks() {
        return chooseReasonRemarks;
    }

    public void setChooseReasonRemarks(String chooseReasonRemarks) {
        this.chooseReasonRemarks = chooseReasonRemarks == null ? null : chooseReasonRemarks.trim();
    }

    public String getKnowChannel() {
        return knowChannel;
    }

    public void setKnowChannel(String knowChannel) {
        this.knowChannel = knowChannel == null ? null : knowChannel.trim();
    }

    public String getKnowChannelDetail() {
        return knowChannelDetail;
    }

    public void setKnowChannelDetail(String knowChannelDetail) {
        this.knowChannelDetail = knowChannelDetail == null ? null : knowChannelDetail.trim();
    }

    public String getKnowChannelRemarks() {
        return knowChannelRemarks;
    }

    public void setKnowChannelRemarks(String knowChannelRemarks) {
        this.knowChannelRemarks = knowChannelRemarks == null ? null : knowChannelRemarks.trim();
    }
}