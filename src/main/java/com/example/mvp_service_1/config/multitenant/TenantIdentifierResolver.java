package com.example.mvp_service_1.config.multitenant;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver {

    public String resolve(HttpServletRequest request) {
        String host = request.getHeader("Host");
        if (host != null) {
            String[] parts = host.split("\\.", -1);
            if (parts.length > 1) {
                return parts[0];
            }
        }

        String header = request.getHeader("X-Tenant-Id");
        if (header != null && !header.isBlank()) {
            return header;
        }

        throw new IllegalStateException("Unable to resolve tenant");
    }
}