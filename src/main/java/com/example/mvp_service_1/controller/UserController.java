package com.example.mvp_service_1.controller;

import com.example.mvp_service_1.config.multitenant.TenantContext;
import com.example.mvp_service_1.model.User;
import com.example.mvp_service_1.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return repo.save(user);
    }

    @PreAuthorize("hasAnyRole('admin','tenant-leader','standard-user')")
    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public Iterable<User> all() {
        return repo.findAll();
    }

    @GetMapping("/active-tenant")
    public String activeTenant() {
        return TenantContext.getCurrentTenant();
    }
}
