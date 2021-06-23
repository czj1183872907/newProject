package com.chery.exeed.crm.lead.dto;

import com.chery.exeed.crm.lead.model.PreLead;

import java.util.Date;

public class PreLeadDTO extends PreLead {
    private String leadNo;
    private String level;
    private String channel;

    private Date testDrivePlan;

    private String campaignId;

    private Integer taskStatus;

    private Date collectTime;

    private String memo;

    private Integer importFlag;

    public Integer getImportFlag() {
        return importFlag;
    }

    public void setImportFlag(Integer importFlag) {
        this.importFlag = importFlag;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Date getTestDrivePlan() {
        return testDrivePlan;
    }

    public void setTestDrivePlan(Date testDrivePlan) {
        this.testDrivePlan = testDrivePlan;
    }

    @Override
    public String getCampaignId() {
        return campaignId;
    }

    @Override
    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getLeadNo() {
        return leadNo;
    }

    public void setLeadNo(String leadNo) {
        this.leadNo = leadNo;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }
}
