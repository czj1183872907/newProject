package com.chery.exeed.crm.lead.model;

import java.util.Date;

public class LeadChannel {
    private Integer id;

    private Integer leadId;

    private Integer channelId;

    private Integer subChannelId;

    private Date createDate;

    private String actCode;

    private String channelCode;

    private String campaignCode;

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

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getSubChannelId() {
        return subChannelId;
    }

    public void setSubChannelId(Integer subChannelId) {
        this.subChannelId = subChannelId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getActCode() {
        return actCode;
    }

    public void setActCode(String actCode) {
        this.actCode = actCode == null ? null : actCode.trim();
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(String campaignCode) {
        this.campaignCode = campaignCode == null ? null : campaignCode.trim();
    }
}