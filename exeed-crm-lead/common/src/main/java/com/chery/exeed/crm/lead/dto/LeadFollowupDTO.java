package com.chery.exeed.crm.lead.dto;

import com.chery.exeed.crm.lead.model.LeadFollowup;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/2/24 15:23
 * @Description:
 */
public class LeadFollowupDTO extends LeadFollowup {

    //客户姓名，线索分级，线索子渠道，预计跟进时间，跟进人
    private Integer custId;

    private String custName;
    private Integer gender;
    //跟进人
    private Integer owner;

    private String ownerName;
    //一级渠道
    private Integer channelId;

    private String channelName;
    //线索子渠道
    private Integer subChannelId;

    private String subChannelName;
    //线索分级
    private String level;


    private String leadStatusDesc;
    private String createByDesc;
    private String distributeToDesc;
    private String modelName;
    private String seriesName;
    private String followMessDesc;
    private String followupDesc;
    private Long toExpiredTime;
    private Integer leadStatus;
    private Integer followupTimes;
    private Integer arrivalTimes;
    private Integer predictTimes;
    private String customerStatus;
    private String theKeyInformation;
    private Integer  total;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getTheKeyInformation() {
        return theKeyInformation;
    }

    public void setTheKeyInformation(String theKeyInformation) {
        this.theKeyInformation = theKeyInformation;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public Integer getFollowupTimes() {
        return followupTimes;
    }

    public void setFollowupTimes(Integer followupTimes) {
        this.followupTimes = followupTimes;
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

    public String getFollowMessDesc() {
        return followMessDesc;
    }

    public void setFollowMessDesc(String followMessDesc) {
        this.followMessDesc = followMessDesc;
    }

    public String getFollowupDesc() {
        return followupDesc;
    }

    public void setFollowupDesc(String followupDesc) {
        this.followupDesc = followupDesc;
    }

    public String getLeadStatusDesc() {
        return leadStatusDesc;
    }

    public void setLeadStatusDesc(String leadStatusDesc) {
        this.leadStatusDesc = leadStatusDesc;
    }

    public String getCreateByDesc() {
        return createByDesc;
    }

    public void setCreateByDesc(String createByDesc) {
        this.createByDesc = createByDesc;
    }

    public String getDistributeToDesc() {
        return distributeToDesc;
    }

    public void setDistributeToDesc(String distributeToDesc) {
        this.distributeToDesc = distributeToDesc;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getSubChannelId() {
        return subChannelId;
    }

    public void setSubChannelId(Integer subChannelId) {
        this.subChannelId = subChannelId;
    }

    public String getSubChannelName() {
        return subChannelName;
    }

    public void setSubChannelName(String subChannelName) {
        this.subChannelName = subChannelName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Long getToExpiredTime() {
        if( super.getFollowPlan() != null){
            long plan = super.getFollowPlan().getTime();
            long now = System.currentTimeMillis();
            return (plan-now);
        }
        return null;
    }

    public void setToExpiredTime(Long toExpiredTime) {
        this.toExpiredTime = toExpiredTime;
    }

    @Override
    public Integer getLeadStatus() {
        return leadStatus;
    }

    @Override
    public void setLeadStatus(Integer leadStatus) {
        this.leadStatus = leadStatus;
    }
}

