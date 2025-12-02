package com.example.mvp_service_1.controller;

import com.example.mvp_service_1.config.multitenant.TenantContext;
import com.example.mvp_service_1.model.User;
import com.example.mvp_service_1.model.dtos.UserFullDetailsResponse;
import com.example.mvp_service_1.services.UserService;
import com.example.mvp_service_1.services.impl.UserDetailsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;

    public UserController(UserService userService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping
    public User create(@RequestBody UserFullDetailsResponse user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public Iterable<User> all() {
        return userService.getAll();
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PreAuthorize("hasAnyRole('admin','manager')")
    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @GetMapping("/{id}/details")
    public UserFullDetailsResponse userProfile(@PathVariable Long id) {
        return userDetailsService.getFullDetails(id,TenantContext.getCurrentTenant());
    }

    @GetMapping("/active-tenant")
    public String activeTenant() {
        return TenantContext.getCurrentTenant();
    }

    @GetMapping("/{id}/roles")
    public List<String> getUserRoles(@PathVariable Long id) {
        return userService.getUserRoles(id);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/{userId}/roles/{roleName}")
    public void assignRole(@PathVariable Long userId, @PathVariable String roleName) {
        userService.assignRole(userId, roleName);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{userId}/roles/{roleName}")
    public void removeRole(@PathVariable Long userId, @PathVariable String roleName) {
        userService.removeRole(userId, roleName);
    }
}
