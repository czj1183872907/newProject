package com.chery.exeed.crm.lead.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2020/12/24 18:36
 * @Description:
 */
@Data
public class MetaMbmDTO implements Serializable {
    private String module;

    private String moduleType;

    private String moduleName;

    private String moduleTypeName;

    private List<MetaMbmDTO> subList;
}