package com.upload.demo.biz;

import com.upload.demo.entity.UserEntity;
import org.springframework.stereotype.Service;


public interface UserService {
    public UserEntity searchUserByNo(String userNo);
}
