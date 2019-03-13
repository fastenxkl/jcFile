package com.upload.demo.dao;

import com.upload.demo.entity.LogEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface LogDaoMapper {
    public int insertLog(LogEntity logEntity);
    public List<LogEntity> selectLogInfoInWeek(Date date);
    public int getOperationFileCountWeekly(Date date,String operation);
}
