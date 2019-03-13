/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: LogServiceImpl
 * Author:   00056929
 * Date:     2018/12/27 9:04
 * Description: 实现类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.upload.demo.biz.impl;

import com.upload.demo.biz.LogService;
import com.upload.demo.dao.LogDaoMapper;
import com.upload.demo.entity.LogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈实现类〉
 *
 * @author care.xu
 * @create 2018/12/27
 * @since 1.0.0
 */
@Service(value = "logSerivce")
public class LogServiceImpl implements LogService {

    @Autowired
    private LogDaoMapper logDao;

    @Override
    public int insertLog(LogEntity logEntity){
        return logDao.insertLog(logEntity);
    }

    @Override
    public int getOperationFileCountWeekly (Date date, String operation) {
        return logDao.getOperationFileCountWeekly(date,operation);

    }
}
