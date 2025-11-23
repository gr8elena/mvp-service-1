package com.example.mvp_service_1.services.impl;

import com.example.mvp_service_1.clients.UserProfileClient;
import com.example.mvp_service_1.model.User;
import com.example.mvp_service_1.model.dtos.UserFullDetailsResponse;
import com.example.mvp_service_1.model.dtos.UserProfileResponse;
import com.example.mvp_service_1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService {

    private final UserRepository userRepository;
    private final UserProfileClient userProfileClient;

    public UserFullDetailsResponse getFullDetails(Long userId, String tenant) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileResponse profile = userProfileClient.getUserProfile(userId, tenant);

        return UserFullDetailsResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .education(profile.getEducation())
                .build();
    }
}
