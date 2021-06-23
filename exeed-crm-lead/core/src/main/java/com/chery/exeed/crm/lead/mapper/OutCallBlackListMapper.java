package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.dto.OutCallBlackListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OutCallBlackListMapper {
    //新增外呼黑名单信息
    Integer insertOutCallBlackList(OutCallBlackListDTO outCallBlackListDTO);

    //删除外呼黑名单信息
    Integer deleteOutCallBlackListById(Integer id);

    //查询外呼黑名单信息
    List<OutCallBlackListDTO> getOutCallBlackList(@Param("phone") String phone,@Param("pageNo")Integer pageNo,@Param("pageSize")Integer pageSize);

    Integer getOutCallBlackListTotal(@Param("phone") String phone);
}