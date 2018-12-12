/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UserEntity
 * Author:   00056929
 * Date:     2018/9/29 15:57
 * Description: 用户名实体类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.upload.demo.entity;

import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br> 
 * 〈用户名实体类〉
 *
 * @author care.xu
 * @create 2018/9/29
 * @since 1.0.0
 */
@Component
public class UserEntity {
    //用户id
    private String userId;
    //用户名
    private String userName;
    //用户密码
    private String password;
    //用户描述
    private String userDesc;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }
}
