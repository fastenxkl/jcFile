/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UserServiceImpl
 * Author:   00056929
 * Date:     2018/12/13 14:04
 * Description: UserServiceImpl
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.upload.demo.biz.impl;

import com.upload.demo.biz.UserService;
import com.upload.demo.dao.UserDaoMapper;
import com.upload.demo.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br> 
 * 〈UserServiceImpl〉
 *
 * @author care.xu
 * @create 2018/12/13
 * @since 1.0.0
 */
@Service(value = "userSerivce")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDaoMapper userDaoMapper;

    @Override
    public UserEntity searchUserByNo(String userId) {
        return userDaoMapper.searchUserByNo(userId);
    }
}
