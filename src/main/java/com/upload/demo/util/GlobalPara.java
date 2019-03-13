/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: GlobalPara
 * Author:   00056929
 * Date:     2018/12/18 8:25
 * Description: 公共参数类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.upload.demo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 〈一句话功能简述〉<br> 
 * 〈公共参数类〉
 *
 * @author care.xu
 * @create 2018/12/18
 * @since 1.0.0
 */
public class GlobalPara {
    public static final String CONFIG_FILE_PATH = "/config.properties";
    public static final String WAFERMAP_URL = "";
    public static final String LOGIN = "login";
    public static final String UPLOAD = "upload";
    public static final String DELETE = "delete";
    public static final String CREATE = "create_directory";
    public static final String REFRESH = "refresh";
    public static final String FTP = "ftp";
    public static final String SMB = "smb";


    private static Properties prop;
    public static Map sysPropertiesMap = new HashMap();

    public static void loadFile() {
        try {
            InputStream in = GlobalPara.class.getResourceAsStream(CONFIG_FILE_PATH);
            prop.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        String resultStr = "";
        /*
        青山附近有个青山镇，规模不是很大，估摸着有十几户人家，大多都是猎户屠户，今日正是元宵节，小镇唯一的街上也挂起了灯笼，更有好几个游方货郎，支着棚子，向镇民们兜售着各自稀罕物件。
        条理：南蛮荒蛊，活命，
        剑仙偶遇，要求收徒，守墓三年，据不答应。药师诊脉：南蛮荒蛊，药石难医，唯有吊命，十年破境，方能有救。
        元宵佳节，初遇药徒，天阴之日，组队前往，护墓家族，奋力抵抗，误入诡墓，偶得珍药，沙上并禽池上暝，云破月来花弄影；风不定，人初静
         */
        if (sysPropertiesMap.get(key) != null) {
            resultStr = String.valueOf(sysPropertiesMap.get(key));
        } else {
            if (prop == null) {
                loadFile();
            }
            try {
                resultStr = prop.getProperty(key);
                if (resultStr != null) {
                    resultStr = resultStr.trim();
                    sysPropertiesMap.put(key,resultStr);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultStr;
    }
}
