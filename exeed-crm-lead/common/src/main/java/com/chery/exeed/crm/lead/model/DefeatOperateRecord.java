package com.chery.exeed.crm.lead.model;

import java.util.Date;

public class DefeatOperateRecord {
    private Integer id;

    private Integer leadId;

    private Integer loseType;

    private Integer loseType2;

    private String competingGoods;

    private String feedback;

    private Date operateTime;

    private Integer operateBy;

    private Integer leadStatus;

    private String loseTypeName;

    private String loseType2Name;

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

    public Integer getLoseType() {
        return loseType;
    }

    public void setLoseType(Integer loseType) {
        this.loseType = loseType;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback == null ? null : feedback.trim();
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

    public Integer getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(Integer leadStatus) {
        this.leadStatus = leadStatus;
    }

    public String getCompetingGoods() {
        return competingGoods;
    }

    public void setCompetingGoods(String competingGoods) {
        this.competingGoods = competingGoods;
    }

    public Integer getLoseType2() {
        return loseType2;
    }

    public void setLoseType2(Integer loseType2) {
        this.loseType2 = loseType2;
    }

    public String getLoseTypeName() {
        return loseTypeName;
    }

    public void setLoseTypeName(String loseTypeName) {
        this.loseTypeName = loseTypeName;
    }

    public String getLoseType2Name() {
        return loseType2Name;
    }

    public void setLoseType2Name(String loseType2Name) {
        this.loseType2Name = loseType2Name;
    }
}