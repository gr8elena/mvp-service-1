package com.example.mvp_service_1.services.impl;

import com.example.mvp_service_1.model.User;
import com.example.mvp_service_1.services.impl.KeycloakAdminService;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

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

    public void assignRealmRole(String realm, String keycloakUserId, String roleName) {

        var keycloak = adminClient.getClientForRealm(realm);

        var roleRep = keycloak.realm(realm)
                .roles()
                .get(roleName)
                .toRepresentation();

        keycloak.realm(realm)
                .users()
                .get(keycloakUserId)
                .roles()
                .realmLevel()
                .add(Collections.singletonList(roleRep));
    }

    public String getKeycloakId(String realm, User user) {
        return adminClient.getUsersResource(realm)
                .search(user.getEmail())
                .get(0)
                .getId();
    }

    public List<String> getUserRoles(User user, String realm) {
        UsersResource users = adminClient.getUsersResource(realm);
        String keycloakId =  getKeycloakId(realm, user);

        return users.get(keycloakId)
                .roles()
                .realmLevel()
                .listEffective()
                .stream()
                .map(RoleRepresentation::getName)
                .toList();
    }

    public void removeRealmRole(String username, String roleName,  String realm) {
        var usersResource = adminClient.getUsersResource(realm);
        var users = usersResource.search(username);

        for (var user : users) {
            if (user.getUsername().equals(username)) {
                var roleRep = adminClient.getClientForRealm(realm)
                        .realm(realm)
                        .roles()
                        .get(roleName)
                        .toRepresentation();

                usersResource.get(user.getId())
                        .roles()
                        .realmLevel()
                        .remove(Collections.singletonList(roleRep));
                return;
            }
        }

        throw new RuntimeException("User not found in Keycloak: " + username);
    }
}
