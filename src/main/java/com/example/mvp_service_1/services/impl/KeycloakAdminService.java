package com.example.mvp_service_1.services.impl;

import com.example.mvp_service_1.config.KeycloakAdminProperties;
import com.example.mvp_service_1.config.RealmConfig;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.stereotype.Service;

@Service
public class KeycloakAdminService {

    private final KeycloakAdminProperties properties;

    public KeycloakAdminService(KeycloakAdminProperties properties) {
        this.properties = properties;
    }

    public Keycloak getClientForRealm(String realmName) {

        RealmConfig config = properties.getRealms().get(realmName);

        if (config == null) {
            throw new IllegalStateException("No Keycloak admin config for realm: " + realmName);
        }

        return KeycloakBuilder.builder()
                .serverUrl(config.getUrl())
                .realm(realmName)
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret())
                .username(config.getUsername())
                .password(config.getPassword())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }

    public UsersResource getUsersResource(String realmName) {
        return getClientForRealm(realmName)
                .realm(realmName)
                .users();
    }
}

