package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.common.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2020/12/10 16:15
 * @Description:
 */
@Mapper
public interface ExViewMapper {

    SysUserViewDTO selectSysUserByUserId(@Param("userId") Integer userId);

    CampaignChannelViewDTO selectCampaignChannelByChannelCode(@Param("channelCode") String channelCode);

    CampaignActViewDTO selectCampaignActByActCode(@Param("actCode") String actCode);

    RegionViewDTO selectRegionByRegionId(@Param("regionId") String regionId);

    List<CarSeriesViewDTO> selectCarSeries();

    List<CarModelViewDTO> selectCarModelBySeriesCode(@Param("seriesCode") String seriesCode);

    DealerViewDTO selectDealerByDealerCode(@Param("dealerCode") String dealerCode);

    List<CallSummaryViewDTO> selectCallSummaryByTaskId(@Param("taskId") Long taskId);
}
