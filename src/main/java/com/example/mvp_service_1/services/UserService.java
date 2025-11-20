package com.example.mvp_service_1.services;

import com.example.mvp_service_1.model.User;

public interface UserService {
    User getUserById(Long userId);
    User createUser(User user);
    void deleteUser(Long userId);
    User updateUser(Long userId, User user);
    Iterable<User> getAll();
}
