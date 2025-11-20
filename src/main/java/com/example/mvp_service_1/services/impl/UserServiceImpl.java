package com.example.mvp_service_1.services.impl;

import com.example.mvp_service_1.config.multitenant.TenantContext;
import com.example.mvp_service_1.model.User;
import com.example.mvp_service_1.repository.UserRepository;
import com.example.mvp_service_1.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KeycloakUserService keycloakUserService;

    public UserServiceImpl(UserRepository userRepository, KeycloakUserService keycloakUserService) {
        this.userRepository = userRepository;
        this.keycloakUserService = keycloakUserService;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User createUser(User user) {
        User userSaved = userRepository.save(user);
        String currentRealm = TenantContext.getCurrentRealm();
        keycloakUserService.createUserInKeycloak(currentRealm, userSaved);
        return userSaved;
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public User updateUser(Long userId, User user) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }

    @Override
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }
}
