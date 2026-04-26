package com.medical360.service.impl;

import com.medical360.entity.Role;
import com.medical360.entity.User;
import com.medical360.mapper.RoleMapper;
import com.medical360.mapper.UserMapper;
import com.medical360.security.JwtTokenProvider;
import com.medical360.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) {
        User user = userMapper.selectByUsername(username);

        // Always perform dummy comparison to normalize timing
        String dummyHash = "$2a$10$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/X4.AKjB4PH.MGiC2y";
        if (user == null) {
            // Still perform BCrypt comparison to prevent timing attack
            passwordEncoder.matches(password, dummyHash);
            throw new RuntimeException("用户名或密码错误");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new RuntimeException("账号已被禁用");
        }

        String roleName = getUserRole(user.getId());

        return jwtTokenProvider.generateToken(
            user.getId(),
            user.getUsername(),
            user.getDepartmentId(),
            roleName
        );
    }

    @Override
    public User getCurrentUser(String username) {
        return userMapper.selectByUsername(username);
    }

    private String getUserRole(Long userId) {
        List<Role> roles = roleMapper.selectRolesByUserId(userId);
        return roles.isEmpty() ? "CLINICIAN" : roles.get(0).getName();
    }
}
