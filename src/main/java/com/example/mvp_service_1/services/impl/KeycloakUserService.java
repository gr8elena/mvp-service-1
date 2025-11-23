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

    public void deleteUserInKeycloak(String realm, String username) {
        var usersResource = adminClient.getUsersResource(realm);
        var users = usersResource.search(username);

        for (var user : users) {
            if (user.getUsername().equals(username)) {
                usersResource.delete(user.getId());
                return;
            }
        }

        throw new RuntimeException("User not found in Keycloak: " + username);
    }

    public void updateUserInKeycloak(String realm, User user) {
        var usersResource = adminClient.getUsersResource(realm);
        var users = usersResource.search(user.getEmail());

        for (var userRep : users) {
            if (userRep.getUsername().equals(user.getEmail())) {
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

                userRep.setFirstName(firstName);
                userRep.setLastName(lastName);
                usersResource.get(userRep.getId()).update(userRep);
                return;
            }
        }

        throw new RuntimeException("User not found in Keycloak: " + user.getEmail());
    }

}
