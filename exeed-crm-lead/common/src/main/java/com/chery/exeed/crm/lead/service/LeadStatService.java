package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.lead.dto.ConsultantLeadCountDTO;
import com.chery.exeed.crm.lead.dto.ConsultantStatDTO;
import com.chery.exeed.crm.lead.dto.LevelStatDTO;
import com.chery.exeed.crm.lead.dto.ManagerLeadStatDTO;

import java.util.List;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/2/20 17:20
 * @Description:
 */
public interface LeadStatService {
    /**
     * 销售助手订单经理首页 线索跟进 信息
     * @return
     */
    ResponseData<ManagerLeadStatDTO> salesAssLeadStat();

    ResponseData<List<ConsultantStatDTO>> salesAssConsultantStat();

    /**
     * 订单经理 销售线索-销售顾问列表-线索数量统计
     * @return
     */
    ResponseData<List<ConsultantLeadCountDTO>> consultantLeadCountStat(List<Integer> consultantIdList);

    /**
     * 销售助手首页-意向级别报表统计
     * @param dealer
     * @param owner
     * @return
     */
    ResponseData<List<LevelStatDTO>> levelStat(String dealer, Integer owner);

    ResponseData<ConsultantLeadCountDTO> dealerLeadCountOthers();
}
