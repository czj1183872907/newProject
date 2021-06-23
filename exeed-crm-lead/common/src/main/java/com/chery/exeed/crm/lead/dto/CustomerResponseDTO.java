package com.chery.exeed.crm.lead.dto;

import java.io.Serializable;

/**
 * @Auther: yueyun.pan
 * @Date: 2019/3/7 16:19
 * @Description:
 */
public class CustomerResponseDTO implements Serializable {

    private static final long serialVersionUID = 1411877771825605815L;

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
