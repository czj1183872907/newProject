package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.dto.VXTestDriveDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VXTestDriveMapper {

    List<VXTestDriveDTO> list();

    int addVxTestDrive(@Param("id")String id,@Param("carType")String carType,@Param("status")Integer status);

    int updateVxTestDrive(@Param("id")String id,@Param("carType")String carType,@Param("status")Integer status);

    Integer queryMaxId();
}