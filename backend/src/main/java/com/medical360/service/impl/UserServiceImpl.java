package com.medical360.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medical360.dto.UserDTO;
import com.medical360.entity.User;
import com.medical360.mapper.RoleMapper;
import com.medical360.mapper.UserMapper;
import com.medical360.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> listUsers(String keyword, Long departmentId, Boolean enabled) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like(User::getUsername, keyword).or().like(User::getName, keyword));
        }
        if (departmentId != null) {
            wrapper.eq(User::getDepartmentId, departmentId);
        }
        if (enabled != null) {
            wrapper.eq(User::getEnabled, enabled);
        }
        wrapper.orderByDesc(User::getCreatedAt);

        List<User> users = userMapper.selectList(wrapper);
        return users.stream().map(this::toDTO).toList();
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return toDTO(user);
    }

    @Override
    @Transactional
    public User createUser(User user, List<Long> roleIds) {
        if (userMapper.existsByUsername(user.getUsername(), -1L)) {
            throw new RuntimeException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode("123456"));
        userMapper.insert(user);
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                userMapper.insertUserRole(user.getId(), roleId);
            }
        }
        return user;
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user, List<Long> roleIds) {
        User existing = userMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("用户不存在");
        }
        if (userMapper.existsByUsername(user.getUsername(), id)) {
            throw new RuntimeException("用户名已存在");
        }
        existing.setUsername(user.getUsername());
        existing.setName(user.getName());
        existing.setDepartmentId(user.getDepartmentId());
        existing.setPositionId(user.getPositionId());
        existing.setEnabled(user.getEnabled());
        userMapper.updateById(existing);

        if (roleIds != null) {
            userMapper.deleteUserRoles(id);
            for (Long roleId : roleIds) {
                userMapper.insertUserRole(id, roleId);
            }
        }
        return existing;
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }

    @Override
    public void setUserEnabled(Long id, boolean enabled) {
        userMapper.updateEnabled(id, enabled);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userMapper.deleteUserRoles(id);
        userMapper.deleteById(id);
    }

    @Override
    public List<UserDTO> listAllUsers() {
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<>(User.class));
        return users.stream().map(this::toDTO).toList();
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setDepartmentId(user.getDepartmentId());
        dto.setPositionId(user.getPositionId());
        dto.setEnabled(user.getEnabled());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setRoles(userMapper.selectRolesByUserId(user.getId()));
        return dto;
    }
}
