package com.medical360.service.impl;

import com.medical360.dto.RoleDTO;
import com.medical360.entity.Role;
import com.medical360.mapper.RoleMapper;
import com.medical360.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    @Override
    public List<RoleDTO> listAllRoles() {
        List<Role> roles = roleMapper.selectAllRoles();
        return roles.stream().map(this::toDTO).toList();
    }

    @Override
    public RoleDTO getRoleById(Long id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        return toDTO(role);
    }

    @Override
    @Transactional
    public Role createRole(Role role, List<String> menuCodes) {
        roleMapper.insert(role);
        if (menuCodes != null && !menuCodes.isEmpty()) {
            for (String menuCode : menuCodes) {
                roleMapper.insertRoleMenu(role.getId(), menuCode);
            }
        }
        return role;
    }

    @Override
    @Transactional
    public Role updateRole(Long id, Role role, List<String> menuCodes) {
        Role existing = roleMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("角色不存在");
        }
        existing.setName(role.getName());
        existing.setDescription(role.getDescription());
        roleMapper.updateById(existing);

        if (menuCodes != null) {
            roleMapper.deleteRoleMenus(id);
            for (String menuCode : menuCodes) {
                roleMapper.insertRoleMenu(id, menuCode);
            }
        }
        return existing;
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        roleMapper.deleteRoleMenus(id);
        roleMapper.deleteById(id);
    }

    private RoleDTO toDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        dto.setMenuCodes(roleMapper.selectMenuCodesByRoleId(role.getId()));
        return dto;
    }
}
