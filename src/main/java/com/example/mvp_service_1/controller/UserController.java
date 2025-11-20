package com.example.mvp_service_1.controller;

import com.example.mvp_service_1.config.multitenant.TenantContext;
import com.example.mvp_service_1.model.User;
import com.example.mvp_service_1.repository.UserRepository;
import com.example.mvp_service_1.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PreAuthorize("hasAnyRole('admin','tenant-leader','standard-user')")
    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public Iterable<User> all() {
        return userService.getAll();
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PreAuthorize("hasRole('admin')")
    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @GetMapping("/active-tenant")
    public String activeTenant() {
        return TenantContext.getCurrentTenant();
    }
}
