package com.chery.exeed.crm.lead.dto;

import com.chery.exeed.crm.lead.model.Lead;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

public class LeadDTO extends Lead implements Serializable {
    private Integer pageNo;
    private Integer pageSize;

    private String createByDesc;

    private Integer channelId;

    private String custName;

    private String custHeadImage;

    private String dealerName;

    private String ratingName;

    private String leadStatusDesc;

    private String searchCode;

    private Date followPlan;

    private Date followDate;

    private Integer assistantResourceType;

    private Integer haveTask;

    //战败类别
    private String loseType;

    //客户反馈
    private String feedback;

    //跟进次数
    private Integer followupTimes;

    //渠道来源次数
    private Integer channelResourceTimes;

    private String campaignCode;

    private String activityCode;

    private String channelCode;

    private String followCarDes;

    private String keyword;

    private String createDate;

    private String followInfoDes;

    private String ownerName;

    private String isFosterManager;

    private Integer currentUserId;

    private Integer pageStart;

    private Integer signNum;

    public Integer getSignNum() {
        return signNum;
    }

    public void setSignNum(Integer signNum) {
        this.signNum = signNum;
    }

    public Integer getPageStart() {
        return pageStart == null ? 0 : pageStart;
    }

    public void setPageStart(Integer pageStart) {
        this.pageStart = pageStart;
    }

    public String getIsFosterManager() {

        return isFosterManager;
    }

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void setIsFosterManager(String isFosterManager) {
        this.isFosterManager = isFosterManager;
    }

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

    public Integer getPageNo() {
        return pageNo==null?1:pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize==null?20:pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getLeadStatusDesc() {
        return leadStatusDesc;
    }

    public void setLeadStatusDesc(String leadStatusDesc) {
        this.leadStatusDesc = leadStatusDesc;
    }

    public String getSearchCode() {
        return searchCode;
    }

    public void setSearchCode(String searchCode) {
        this.searchCode = searchCode;
    }

    public String getCreateByDesc() {
        return createByDesc;
    }

    public void setCreateByDesc(String createByDesc) {
        this.createByDesc = createByDesc;
    }

    public String getCustHeadImage() {
        return custHeadImage;
    }

    public void setCustHeadImage(String custHeadImage) {
        this.custHeadImage = custHeadImage;
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


    public Integer getChannelResourceTimes() {
        return channelResourceTimes;
    }

    public void setChannelResourceTimes(Integer channelResourceTimes) {
        this.channelResourceTimes = channelResourceTimes;
    }

    public Integer getAssistantResourceType() {
        return assistantResourceType;
    }

    public void setAssistantResourceType(Integer assistantResourceType) {
        this.assistantResourceType = assistantResourceType;
    }

    public Integer getHaveTask() {
        return haveTask;
    }

    public void setHaveTask(Integer haveTask) {
        this.haveTask = haveTask;
    }

    public Integer getFollowupTimes() {
        return followupTimes;
    }

    public void setFollowupTimes(Integer followupTimes) {
        this.followupTimes = followupTimes;
    }

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(String campaignCode) {
        this.campaignCode = campaignCode;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }


}
