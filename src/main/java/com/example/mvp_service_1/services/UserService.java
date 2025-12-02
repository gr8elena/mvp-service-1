package com.example.mvp_service_1.services;

import com.example.mvp_service_1.model.User;
import com.example.mvp_service_1.model.dtos.UserFullDetailsResponse;

import java.util.List;

public interface UserService {
    User getUserById(Long userId);
    User createUser(UserFullDetailsResponse user);
    void deleteUser(Long userId);
    User updateUser(Long userId, User user);
    Iterable<User> getAll();
    List<String> getUserRoles(Long userId);
    void assignRole(Long userId, String roleName);
    void removeRole(Long userId, String roleName);
}
