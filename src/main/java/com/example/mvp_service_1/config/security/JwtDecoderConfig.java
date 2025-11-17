package com.example.mvp_service_1.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JwtDecoderConfig {

    @Bean
    public Map<String, JwtDecoder> tenantJwtDecoders() {
        Map<String, JwtDecoder> map = new HashMap<>();

        map.put("tenant1", JwtDecoders.fromIssuerLocation("http://localhost:9090/realms/tenant1-realm"));
        map.put("tenant2", JwtDecoders.fromIssuerLocation("http://localhost:9090/realms/tenant2-realm"));

        return map;
    }
}
