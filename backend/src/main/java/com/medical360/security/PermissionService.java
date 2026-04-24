package com.medical360.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medical360.entity.Role;
import com.medical360.entity.User;
import com.medical360.mapper.RoleMapper;
import com.medical360.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    public boolean canAccessPatient(String username, Long patientId) {
        User user = userMapper.selectByUsername(username);
        if (user == null) return false;
        if (isAdmin(user)) return true;
        return true;
    }

    public boolean hasMenuPermission(String username, String menuCode) {
        User user = userMapper.selectByUsername(username);
        if (user == null) return false;
        if (isAdmin(user)) return true;
        return true;
    }

    private boolean isAdmin(User user) {
        List<Role> roles = roleMapper.selectRolesByUserId(user.getId());
        return roles.stream().anyMatch(r -> "SYS_ADMIN".equals(r.getName()));
    }

    public Long getUserDepartmentId(String username) {
        User user = userMapper.selectByUsername(username);
        return user != null ? user.getDepartmentId() : null;
    }

    public String getUserRole(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) return null;
        List<Role> roles = roleMapper.selectRolesByUserId(user.getId());
        return roles.isEmpty() ? null : roles.get(0).getName();
    }
}
