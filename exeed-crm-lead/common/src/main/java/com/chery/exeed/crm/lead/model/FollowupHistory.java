package com.chery.exeed.crm.lead.model;

import java.util.Date;

public class FollowupHistory {
    private Integer id;

    private Integer leadId;

    private Integer bizType;

    private Integer bizId;

    private Date operateTime;

    private String operateDesc;

    private String operateBy;

    private Integer operateResultStatus;

    private String operateResultDesc;

    private String extraInfo;

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

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public Integer getBizId() {
        return bizId;
    }

    public void setBizId(Integer bizId) {
        this.bizId = bizId;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateDesc() {
        return operateDesc;
    }

    public void setOperateDesc(String operateDesc) {
        this.operateDesc = operateDesc == null ? null : operateDesc.trim();
    }

    public String getOperateBy() {
        return operateBy;
    }

    public void setOperateBy(String operateBy) {
        this.operateBy = operateBy == null ? null : operateBy.trim();
    }

    public Integer getOperateResultStatus() {
        return operateResultStatus;
    }

    public void setOperateResultStatus(Integer operateResultStatus) {
        this.operateResultStatus = operateResultStatus;
    }

    public String getOperateResultDesc() {
        return operateResultDesc;
    }

    public void setOperateResultDesc(String operateResultDesc) {
        this.operateResultDesc = operateResultDesc == null ? null : operateResultDesc.trim();
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo == null ? null : extraInfo.trim();
    }
}