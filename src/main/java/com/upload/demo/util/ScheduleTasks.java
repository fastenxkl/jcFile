/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ScheduleTasks
 * Author:   00056929
 * Date:     2018/12/29 8:29
 * Description: 定时任务类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.upload.demo.util;

import com.upload.demo.biz.LogService;
import com.upload.demo.controller.UploadController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br> 
 * 〈定时任务类〉
 *
 * @author care.xu
 * @create 2018/12/29
 * @since 1.0.0
 */
@Component
@Configurable
@EnableScheduling
public class ScheduleTasks {

    @Autowired
    private LogService logService;
//    @Scheduled(fixedRate = 1000 * 30)
//    public void reportCurrentTime(){
//        System.out.println ("Scheduling Tasks Examples: The time is now " + dateFormat ().format (new Date()));
//    }

    //每1分钟执行一次
    @Scheduled(cron = "30 * *  * * * ")
    public void reportCurrentByCron(){
        System.out.println ("Scheduling Tasks Examples By Cron: The time is now " + dateFormat ().format (new Date ()));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,-7);
        System.out.println(new Date());
        int num = logService.getOperationFileCountWeekly(new Date(),GlobalPara.UPLOAD);
        System.out.println("num :" + num);

    }

    private SimpleDateFormat dateFormat(){
        return new SimpleDateFormat ("HH:mm:ss");
    }

}