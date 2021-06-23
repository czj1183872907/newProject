package com.chery.exeed.crm.lead.dto;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/2/20 17:33
 * @Description:
 */
public class ConsultantStatDTO {
    private Integer owner;

    private Integer countDone;

    private Integer countDoing;

    private Integer countExpired;

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public Integer getCountDone() {
        return countDone;
    }

    public void setCountDone(Integer countDone) {
        this.countDone = countDone;
    }

    public Integer getCountDoing() {
        return countDoing;
    }

    public void setCountDoing(Integer countDoing) {
        this.countDoing = countDoing;
    }

    public Integer getCountExpired() {
        return countExpired;
    }

    public void setCountExpired(Integer countExpired) {
        this.countExpired = countExpired;
    }
}
