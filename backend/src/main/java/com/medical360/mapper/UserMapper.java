package com.medical360.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical360.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT u.*, r.name as role_name FROM users u " +
            "LEFT JOIN user_role ur ON u.id = ur.user_id " +
            "LEFT JOIN role r ON ur.role_id = r.id " +
            "WHERE u.username = #{username}")
    User selectByUsername(String username);
}
