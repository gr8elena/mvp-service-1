package com.example.mvp_service_1.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileCreateRequest {
    private Long userId;
    private String education;
}
