package com.example.mvp_service_1.services.impl;

import com.example.mvp_service_1.clients.UserProfileClient;
import com.example.mvp_service_1.config.multitenant.TenantContext;
import com.example.mvp_service_1.model.User;
import com.example.mvp_service_1.model.dtos.UserFullDetailsResponse;
import com.example.mvp_service_1.model.dtos.UserProfileCreateRequest;
import com.example.mvp_service_1.repository.UserRepository;
import com.example.mvp_service_1.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KeycloakUserService keycloakUserService;
    private final UserProfileClient userProfileClient;

    public UserServiceImpl(UserRepository userRepository, KeycloakUserService keycloakUserService, UserProfileClient userProfileClient) {
        this.userRepository = userRepository;
        this.keycloakUserService = keycloakUserService;
        this.userProfileClient = userProfileClient;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User createUser(UserFullDetailsResponse userFullDetails) {
        User user = User.builder()
                .name(userFullDetails.getName())
                .email(userFullDetails.getEmail())
                .build();

        User saved = userRepository.save(user);

        String currentRealm = TenantContext.getCurrentRealm();
        keycloakUserService.createUserInKeycloak(currentRealm, saved);

        UserProfileCreateRequest req = new UserProfileCreateRequest(
                saved.getId(),
                userFullDetails.getEducation()
        );

        String tenant = TenantContext.getCurrentTenant();
        userProfileClient.createUserProfile(req, tenant);

        return saved;
    }


    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
        String currentRealm = TenantContext.getCurrentRealm();
        keycloakUserService.deleteUserInKeycloak(currentRealm, user.getEmail());
        String tenant = TenantContext.getCurrentTenant();
        userProfileClient.deleteUserProfile(userId, tenant);
    }

    @Override
    public User updateUser(Long userId, User user) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        User updatedUser = userRepository.save(existingUser);
        String currentRealm = TenantContext.getCurrentRealm();
        keycloakUserService.updateUserInKeycloak(currentRealm, existingUser);
        return updatedUser;
    }

    public void assignRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String realm = TenantContext.getCurrentRealm();
        String keycloakUserId = keycloakUserService.getKeycloakId(realm, user);

        keycloakUserService.assignRealmRole(realm, keycloakUserId, roleName);
    }

    @Override
    public void removeRole(Long userId, String roleName) {
        String realm = TenantContext.getCurrentRealm();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        keycloakUserService.removeRealmRole(user.getEmail(), roleName, realm);
    }

    @Override
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<String> getUserRoles(Long userId) {
        String realm = TenantContext.getCurrentRealm();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return keycloakUserService.getUserRoles(user, realm);
    }
}
