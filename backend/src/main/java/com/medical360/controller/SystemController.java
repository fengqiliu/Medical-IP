package com.medical360.controller;

import com.medical360.common.Result;
import com.medical360.dto.*;
import com.medical360.entity.*;
import com.medical360.service.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system")
@RequiredArgsConstructor
public class SystemController {

    private final UserService userService;
    private final RoleService roleService;
    private final DepartmentService departmentService;
    private final PositionService positionService;
    private final AccessLogService accessLogService;

    // ==================== 用户管理 ====================

    @GetMapping("/users")
    public Result<List<UserDTO>> listUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Boolean enabled) {
        return Result.success(userService.listUsers(keyword, departmentId, enabled));
    }

    @GetMapping("/users/{id}")
    public Result<UserDTO> getUser(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @PostMapping("/users")
    public Result<User> createUser(@RequestBody UserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setDepartmentId(request.getDepartmentId());
        user.setPositionId(request.getPositionId());
        user.setEnabled(request.getEnabled() != null ? request.getEnabled() : true);
        User created = userService.createUser(user, request.getRoleIds());
        return Result.success(created);
    }

    @PutMapping("/users/{id}")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setDepartmentId(request.getDepartmentId());
        user.setPositionId(request.getPositionId());
        user.setEnabled(request.getEnabled() != null ? request.getEnabled() : true);
        User updated = userService.updateUser(id, user, request.getRoleIds());
        return Result.success(updated);
    }

    @PutMapping("/users/{id}/password")
    public Result<Void> updatePassword(@PathVariable Long id, @RequestBody PasswordRequest request) {
        userService.updatePassword(id, request.getNewPassword());
        return Result.success();
    }

    @PutMapping("/users/{id}/enabled")
    public Result<Void> setUserEnabled(@PathVariable Long id, @RequestParam boolean enabled) {
        userService.setUserEnabled(id, enabled);
        return Result.success();
    }

    @DeleteMapping("/users/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @GetMapping("/users/all")
    public Result<List<UserDTO>> listAllUsers() {
        return Result.success(userService.listAllUsers());
    }

    // ==================== 角色管理 ====================

    @GetMapping("/roles")
    public Result<List<RoleDTO>> listRoles() {
        return Result.success(roleService.listAllRoles());
    }

    @GetMapping("/roles/{id}")
    public Result<RoleDTO> getRole(@PathVariable Long id) {
        return Result.success(roleService.getRoleById(id));
    }

    @PostMapping("/roles")
    public Result<Role> createRole(@RequestBody RoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        Role created = roleService.createRole(role, request.getMenuCodes());
        return Result.success(created);
    }

    @PutMapping("/roles/{id}")
    public Result<Role> updateRole(@PathVariable Long id, @RequestBody RoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        Role updated = roleService.updateRole(id, role, request.getMenuCodes());
        return Result.success(updated);
    }

    @DeleteMapping("/roles/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success();
    }

    // ==================== 部门管理 ====================

    @GetMapping("/departments")
    public Result<List<DepartmentDTO>> listDepartments() {
        return Result.success(departmentService.listAllDepartments());
    }

    @GetMapping("/departments/{id}")
    public Result<DepartmentDTO> getDepartment(@PathVariable Long id) {
        return Result.success(departmentService.getDepartmentById(id));
    }

    @PostMapping("/departments")
    public Result<Department> createDepartment(@RequestBody Department department) {
        Department created = departmentService.createDepartment(department);
        return Result.success(created);
    }

    @PutMapping("/departments/{id}")
    public Result<Department> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        Department updated = departmentService.updateDepartment(id, department);
        return Result.success(updated);
    }

    @DeleteMapping("/departments/{id}")
    public Result<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return Result.success();
    }

    // ==================== 岗位管理 ====================

    @GetMapping("/positions")
    public Result<List<PositionDTO>> listPositions() {
        return Result.success(positionService.listPositions());
    }

    @GetMapping("/positions/{id}")
    public Result<PositionDTO> getPosition(@PathVariable Long id) {
        return Result.success(positionService.getPositionById(id));
    }

    @PostMapping("/positions")
    public Result<Position> createPosition(@RequestBody Position position) {
        Position created = positionService.createPosition(position);
        return Result.success(created);
    }

    @PutMapping("/positions/{id}")
    public Result<Position> updatePosition(@PathVariable Long id, @RequestBody Position position) {
        Position updated = positionService.updatePosition(id, position);
        return Result.success(updated);
    }

    @DeleteMapping("/positions/{id}")
    public Result<Void> deletePosition(@PathVariable Long id) {
        positionService.deletePosition(id);
        return Result.success();
    }

    // ==================== 访问日志 ====================

    @GetMapping("/access-logs")
    public Result<List<AccessLogDTO>> listAccessLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<AccessLogDTO> logs = accessLogService.listAccessLogs(userId, patientId, action, startDate, endDate, page, size);
        long total = accessLogService.countAccessLogs(userId, patientId, action, startDate, endDate);
        return Result.success(logs);
    }

    // ==================== 请求 DTO ====================

    @Data
    public static class UserRequest {
        private String username;
        private String name;
        private Long departmentId;
        private Long positionId;
        private Boolean enabled;
        private List<Long> roleIds;
    }

    @Data
    public static class PasswordRequest {
        private String newPassword;
    }

    @Data
    public static class RoleRequest {
        private String name;
        private String description;
        private List<String> menuCodes;
    }
}
