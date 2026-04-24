package com.medical360.service;

import com.medical360.entity.User;

public interface AuthService {
    String login(String username, String password);
    User getCurrentUser(String username);
}
