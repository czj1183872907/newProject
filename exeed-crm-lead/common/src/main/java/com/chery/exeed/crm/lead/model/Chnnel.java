package com.chery.exeed.crm.lead.model;

import java.util.Date;

public class Chnnel {
    private Integer id;

    private String name;

    private Integer pid;

    private Integer createTask;

    private Integer isSend;

    private Integer createBy;

    private Date createDate;

    private Integer chngBy;

    private Date chngDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getCreateTask() {
        return createTask;
    }

    public void setCreateTask(Integer createTask) {
        this.createTask = createTask;
    }

    public Integer getIsSend() {
        return isSend;
    }

    public void setIsSend(Integer isSend) {
        this.isSend = isSend;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getChngBy() {
        return chngBy;
    }

    public void setChngBy(Integer chngBy) {
        this.chngBy = chngBy;
    }

    public Date getChngDate() {
        return chngDate;
    }

    public void setChngDate(Date chngDate) {
        this.chngDate = chngDate;
    }
}