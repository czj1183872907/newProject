package com.chery.exeed.crm.lead.dto;

import java.io.Serializable;

/**
 * @Description:
 * @author: gang.li@nttdata.com
 * @date: 2019/3/8 10:08
 */
public class LevelDistributionDTO implements Serializable {



    private String level;

    private Integer total;

    private Integer thisMonth;

    private Integer lastMonth;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getThisMonth() {
        return thisMonth;
    }

    public void setThisMonth(Integer thisMonth) {
        this.thisMonth = thisMonth;
    }

    public Integer getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(Integer lastMonth) {
        this.lastMonth = lastMonth;
    }
}

