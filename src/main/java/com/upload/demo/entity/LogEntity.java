/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: LogEntity
 * Author:   00056929
 * Date:     2018/12/27 8:32
 * Description: 用户操作日志实体类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.upload.demo.entity;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br> 
 * 〈用户操作日志实体类〉
 *
 * @author care.xu
 * @create 2018/12/27
 * @since 1.0.0
 */
public class LogEntity {
    public static void test(String id){
        System.out.println("test");
    }
    public String id;
    public String userName;
    public String operation;
    public String fileName;
    public String path;
    public String uploadType;
    public String loginIp;
    public Date uploadTime;

    public LogEntity() {
    }

    public LogEntity(String userName, String operation, String fileName, String path, String uploadType, String loginIp, Date uploadTime) {
        this.userName = userName;
        this.operation = operation;
        this.fileName = fileName;
        this.path = path;
        this.uploadType = uploadType;
        this.loginIp = loginIp;
        this.uploadTime = uploadTime;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
