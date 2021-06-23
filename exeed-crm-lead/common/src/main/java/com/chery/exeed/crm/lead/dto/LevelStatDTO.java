package com.chery.exeed.crm.lead.dto;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/2/25 14:43
 * @Description:
 */
public class LevelStatDTO {
    private Integer level;

    private String levelDesc;

    private Integer statThisMonth;

    private Integer statHistory;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLevelDesc() {
        return levelDesc;
    }

    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc;
    }

    public Integer getStatThisMonth() {
        return statThisMonth;
    }

    public void setStatThisMonth(Integer statThisMonth) {
        this.statThisMonth = statThisMonth;
    }

    public Integer getStatHistory() {
        return statHistory;
    }

    public void setStatHistory(Integer statHistory) {
        this.statHistory = statHistory;
    }
}
