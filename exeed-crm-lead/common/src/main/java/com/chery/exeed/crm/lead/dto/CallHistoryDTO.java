package com.chery.exeed.crm.lead.dto;

import java.util.Date;

public class CallHistoryDTO {
    private Long id;

    private Integer callCategory;

    private String contact;

    private String contactInformation;

    private Date callTime;

    private String callContent;

    private Date createBy;

    private Integer callDuration;

    private String smsContent;

    private String comment;

    private Integer taskId;

    private Integer caseId;

    private String callCategoryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCallCategory() {
        return callCategory;
    }

    public void setCallCategory(Integer callCategory) {
        this.callCategory = callCategory;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation == null ? null : contactInformation.trim();
    }

    public Date getCallTime() {
        return callTime;
    }

    public void setCallTime(Date callTime) {
        this.callTime = callTime;
    }

    public String getCallContent() {
        return callContent;
    }

    public void setCallContent(String callContent) {
        this.callContent = callContent == null ? null : callContent.trim();
    }

    public Date getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Date createBy) {
        this.createBy = createBy;
    }

    public Integer getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(Integer callDuration) {
        this.callDuration = callDuration;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent == null ? null : smsContent.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getCallCategoryName() {
        return callCategoryName;
    }

    public void setCallCategoryName(String callCategoryName) {
        this.callCategoryName = callCategoryName;
    }
}