package com.chery.exeed.crm.lead.model;

import java.util.Date;

public class CustomerMerge {
    private Integer id;

    private Integer fromId;

    private Integer targetId;

    private Date createTime;

    private Integer createBy;

    private String mergeContent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public String getMergeContent() {
        return mergeContent;
    }

    public void setMergeContent(String mergeContent) {
        this.mergeContent = mergeContent == null ? null : mergeContent.trim();
    }
}