/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: HostEntity
 * Author:   00056929
 * Date:     2018/10/22 16:48
 * Description: 服务实体类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.upload.demo.entity;

import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈服务实体类〉
 *
 * @author care.xu
 * @create 2018/10/22
 * @since 1.0.0
 */
public class HostEntity {
    private String hostName;
    private String port;
    private String userName;
    private String password;
    private Map filePath;
    private String hostIndex;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map getFilePath() {
        return filePath;
    }

    public void setFilePath(Map filePath) {
        this.filePath = filePath;
    }

    public String getHostIndex() {
        return hostIndex;
    }

    public void setHostIndex(String hostIndex) {
        this.hostIndex = hostIndex;
    }
}
