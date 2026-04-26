package com.medical360.service;

import com.medical360.dto.UserDTO;
import com.medical360.entity.User;
import java.util.List;

public interface UserService {

    List<UserDTO> listUsers(String keyword, Long departmentId, Boolean enabled);

    UserDTO getUserById(Long id);

    User createUser(User user, List<Long> roleIds);

    User updateUser(Long id, User user, List<Long> roleIds);

    void updatePassword(Long id, String newPassword);

    void setUserEnabled(Long id, boolean enabled);

    void deleteUser(Long id);

    List<UserDTO> listAllUsers();
}
