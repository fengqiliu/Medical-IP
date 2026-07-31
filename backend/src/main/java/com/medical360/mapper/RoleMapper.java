package com.medical360.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical360.entity.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    @Select("SELECT r.* FROM role r " +
            "INNER JOIN user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} ORDER BY r.id")
    List<Role> selectRolesByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM role ORDER BY id")
    List<Role> selectAllRoles();

    @Insert("INSERT INTO role_menu (role_id, menu_code) VALUES (#{roleId}, #{menuCode})")
    int insertRoleMenu(@Param("roleId") Long roleId, @Param("menuCode") String menuCode);

    @Delete("DELETE FROM role_menu WHERE role_id = #{roleId}")
    int deleteRoleMenus(@Param("roleId") Long roleId);

    @Select("SELECT menu_code FROM role_menu WHERE role_id = #{roleId} ORDER BY menu_code")
    List<String> selectMenuCodesByRoleId(@Param("roleId") Long roleId);
}
