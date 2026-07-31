package com.medical360.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical360.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM users WHERE username = #{username}")
    User selectByUsername(@Param("username") String username);

    @Select("SELECT EXISTS (" +
            "SELECT 1 FROM users WHERE username = #{username} AND id <> #{excludeId}" +
            ")")
    boolean existsByUsername(@Param("username") String username, @Param("excludeId") Long excludeId);

    @Insert("INSERT INTO user_role (user_id, role_id) VALUES (#{userId}, #{roleId})")
    int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Delete("DELETE FROM user_role WHERE user_id = #{userId}")
    int deleteUserRoles(@Param("userId") Long userId);

    @Update("UPDATE users SET enabled = #{enabled} WHERE id = #{userId}")
    int updateEnabled(@Param("userId") Long userId, @Param("enabled") boolean enabled);

    @Select("SELECT r.name FROM role r " +
            "INNER JOIN user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} ORDER BY r.id")
    List<String> selectRolesByUserId(@Param("userId") Long userId);
}
