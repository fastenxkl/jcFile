package com.upload.demo.dao;

import com.upload.demo.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserDaoMapper {
    public UserEntity searchUserByNo(String userId);
}
