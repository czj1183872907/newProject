package com.chery.exeed.crm.lead.dto;

import com.chery.exeed.crm.lead.model.PredictOperateRecord;

/**
 * @Description:
 * @author: gang.li@nttdata.com
 * @date: 2019/3/16 13:22
 */
public class PredictOperateRecordDTO extends PredictOperateRecord {
    private String credentialType;

    private String seriesNameDesc;
    private String modelNameDesc;

    private String isSign;

    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    public String getSeriesNameDesc() {
        return seriesNameDesc;
    }

    public void setSeriesNameDesc(String seriesNameDesc) {
        this.seriesNameDesc = seriesNameDesc;
    }

    public String getModelNameDesc() {
        return modelNameDesc;
    }

    public void setModelNameDesc(String modelNameDesc) {
        this.modelNameDesc = modelNameDesc;
    }

    public String getIsSign() {
        return isSign;
    }

    public void setIsSign(String isSign) {
        this.isSign = isSign;
    }
}

