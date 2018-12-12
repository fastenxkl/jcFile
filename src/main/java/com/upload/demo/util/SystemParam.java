/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: SystemParam
 * Author:   00056929
 * Date:     2018/10/9 8:08
 * Description: 系统参数类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.upload.demo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;

/**
 * 〈一句话功能简述〉<br> 
 * 〈系统参数类〉
 *
 * @author care.xu
 * @create 2018/10/9
 * @since 1.0.0
 */
public class SystemParam {
    public static final String CONFIG_FILE_PATH = "/application.properties";
    //备份ftp地址
    public static final String SEND_FTP_IP = "";
    public static final String SEND_FTP_PORT = "";
    public static final String SEND_FTP_NAME = "";
    public static final String SEND_FTP_PSW = "";
    public static final String SEND_FILE_PATH = "";

    //用户发送ftp地址
    public static final String USER_FTP_IP = "";
    public static final String USER_FTP_PORT = "";
    public static final String USER_FTP_NAME = "";
    public static final String USER_FTP_PSW = "";
    public static final String USER_FILE_PATH = "";

    @Value("${default}")
    private String defaultIp;

    public static void main() {
        System.out.println("defaultIp:");
    }




}
