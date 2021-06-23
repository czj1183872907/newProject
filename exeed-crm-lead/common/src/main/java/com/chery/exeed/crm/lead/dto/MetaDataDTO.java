package com.chery.exeed.crm.lead.dto;

/**
 * Author:xiaowei.zhu
 * 2019/1/22 11:31
 */
public class MetaDataDTO implements java.io.Serializable {

    private Integer metaCode;
    private String metaName;
    private String description;

    public Integer getMetaCode() {
        return metaCode;
    }

    public void setMetaCode(Integer metaCode) {
        this.metaCode = metaCode;
    }

    public String getMetaName() {
        return metaName;
    }

    public void setMetaName(String metaName) {
        this.metaName = metaName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
