package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.lead.dto.ImportDTO;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Author:xiaowei.zhu
 * 2019/6/18 18:51
 */

public interface LeadDistributeConfigService {
    ResponseData<String> importConfig(@RequestBody ImportDTO importDTO);
}
