/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: LogService
 * Author:   00056929
 * Date:     2018/12/27 9:01
 * Description: logservice接口
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.upload.demo.biz;

import com.upload.demo.entity.LogEntity;
import com.upload.demo.entity.UserEntity;

import java.util.Date;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈logservice接口〉
 *
 * @author care.xu
 * @create 2018/12/27
 * @since 1.0.0
 */
public interface LogService {
    public int insertLog(LogEntity logEntity);
    public int getOperationFileCountWeekly (Date date, String operation);

}
