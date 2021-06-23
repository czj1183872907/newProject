package com.chery.exeed.crm.lead.dto;

/**
 * @Description:
 * @author: gang.li@nttdata.com
 * @date: 2019/3/8 16:44
 */
public class ConsultantPerformanceDTO {

    private Integer userId;

    private String username;

    private Integer notFollowed;

    private Integer followed;

    private Integer timeout;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getNotFollowed() {
        return notFollowed;
    }

    public void setNotFollowed(Integer notFollowed) {
        this.notFollowed = notFollowed;
    }

    public Integer getFollowed() {
        return followed;
    }

    public void setFollowed(Integer followed) {
        this.followed = followed;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}

