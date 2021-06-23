package com.chery.exeed.crm.lead.model;

public class MetaKnowChannel {
    private String knowChannel;

    private String knowChannelDesc;

    private String knowChannelDetail;

    private String knowChannelDetailDesc;

    public String getKnowChannel() {
        return knowChannel;
    }

    public void setKnowChannel(String knowChannel) {
        this.knowChannel = knowChannel == null ? null : knowChannel.trim();
    }

    public String getKnowChannelDesc() {
        return knowChannelDesc;
    }

    public void setKnowChannelDesc(String knowChannelDesc) {
        this.knowChannelDesc = knowChannelDesc == null ? null : knowChannelDesc.trim();
    }

    public String getKnowChannelDetail() {
        return knowChannelDetail;
    }

    public void setKnowChannelDetail(String knowChannelDetail) {
        this.knowChannelDetail = knowChannelDetail == null ? null : knowChannelDetail.trim();
    }

    public String getKnowChannelDetailDesc() {
        return knowChannelDetailDesc;
    }

    public void setKnowChannelDetailDesc(String knowChannelDetailDesc) {
        this.knowChannelDetailDesc = knowChannelDetailDesc == null ? null : knowChannelDetailDesc.trim();
    }
}