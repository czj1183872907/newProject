package com.chery.exeed.crm.lead.dto;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/2/20 16:14
 * @Description:
 */
public class ManagerLeadStatDTO {

    private Integer undistributed;

    private Integer distributed;

    private Integer defeat;

    public Integer getUndistributed() {
        return undistributed;
    }

    public void setUndistributed(Integer undistributed) {
        this.undistributed = undistributed;
    }

    public Integer getDistributed() {
        return distributed;
    }

    public void setDistributed(Integer distributed) {
        this.distributed = distributed;
    }

    public Integer getDefeat() {
        return defeat;
    }

    public void setDefeat(Integer defeat) {
        this.defeat = defeat;
    }
}
