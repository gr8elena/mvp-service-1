package com.example.mvp_service_1.clients;
import com.example.mvp_service_1.model.dtos.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "profile-service",
        url = "${profiles.service.url}"
)
public interface UserProfileClient {

    @GetMapping("/api/profiles/{userId}")
    UserProfileResponse getUserProfile(@PathVariable Long userId, @RequestHeader("X-Tenant-Id") String tenant);
}