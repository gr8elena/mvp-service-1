package com.example.mvp_service_1.model.dtos;

import lombok.Data;

@Data
public class UserProfileResponse {
    private Long id;
    private Long userId;
    private String name;
    private String surname;
    private String education;
}
