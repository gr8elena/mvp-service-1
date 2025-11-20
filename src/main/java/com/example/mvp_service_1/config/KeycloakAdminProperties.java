package com.example.mvp_service_1.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "keycloak.admin")
public class KeycloakAdminProperties {
    public KeycloakAdminProperties() {
    }

    public Map<String, RealmConfig> getRealms() {
        return realms;
    }

    private final Map<String, RealmConfig> realms = new HashMap<>();
}
