package com.example.mvp_service_1.services.impl;

import com.example.mvp_service_1.model.User;
import com.example.mvp_service_1.services.impl.KeycloakAdminService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service

public class KeycloakUserService {

    private final KeycloakAdminService adminClient;

    public KeycloakUserService(KeycloakAdminService adminClient) {
        this.adminClient = adminClient;
    }

    public void createUserInKeycloak(String realm, User user) {
        String fullName = user.getName();
        String firstName = "";
        String lastName = "";

        if (fullName != null && fullName.contains(" ")) {
            int idx = fullName.indexOf(" ");
            firstName = fullName.substring(0, idx);
            lastName = fullName.substring(idx + 1);
        } else {
            firstName = fullName;
        }

        UserRepresentation rep = new UserRepresentation();
        rep.setEnabled(true);
        rep.setUsername(user.getEmail());
        rep.setEmail(user.getEmail());
        rep.setFirstName(firstName);
        rep.setLastName(lastName);
        rep.setEmailVerified(true);

        Response response = adminClient.getUsersResource(realm).create(rep);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatus());
        }
    }

}
