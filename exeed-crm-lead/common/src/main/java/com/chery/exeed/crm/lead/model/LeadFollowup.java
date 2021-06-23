package com.chery.exeed.crm.lead.model;

import java.util.Date;

public class LeadFollowup {
    private Integer id;

    private Integer leadId;

    private Integer distributeTo;

    private String createBy;

    private Date createTime;

    private String name;

    private Integer followType;

    private Integer status;

    private Date followPlan;

    private Date followDate;

    private Integer leadStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public Integer getDistributeTo() {
        return distributeTo;
    }

    public void setDistributeTo(Integer distributeTo) {
        this.distributeTo = distributeTo;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getFollowType() {
        return followType;
    }

    public void setFollowType(Integer followType) {
        this.followType = followType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(Integer leadStatus) {
        this.leadStatus = leadStatus;
    }
}