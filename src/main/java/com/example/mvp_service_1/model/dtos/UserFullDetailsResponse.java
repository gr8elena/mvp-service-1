package com.example.mvp_service_1.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFullDetailsResponse {
    private Long id;
    private String name;
    private String email;
    private String education;
}

