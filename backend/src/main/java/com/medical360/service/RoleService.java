package com.medical360.service;

import com.medical360.dto.RoleDTO;
import com.medical360.entity.Role;
import java.util.List;

public interface RoleService {

    List<RoleDTO> listAllRoles();

    RoleDTO getRoleById(Long id);

    Role createRole(Role role, List<String> menuCodes);

    Role updateRole(Long id, Role role, List<String> menuCodes);

    void deleteRole(Long id);
}
