package com.chery.exeed.crm.lead.model;

import java.util.Date;

public class DistributeOperateRecord {
    private Integer id;

    private Integer leadId;

    private Integer leadStatus;

    private Integer distributeTo;

    private Date operateTime;

    private Integer operateBy;

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

    public Integer getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(Integer leadStatus) {
        this.leadStatus = leadStatus;
    }

    public Integer getDistributeTo() {
        return distributeTo;
    }

    public void setDistributeTo(Integer distributeTo) {
        this.distributeTo = distributeTo;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getOperateBy() {
        return operateBy;
    }

    public void setOperateBy(Integer operateBy) {
        this.operateBy = operateBy;
    }
}