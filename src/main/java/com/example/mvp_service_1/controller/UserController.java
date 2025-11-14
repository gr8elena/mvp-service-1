package com.example.mvp_service_1.controller;

import com.example.mvp_service_1.model.User;
import com.example.mvp_service_1.repository.UserRepository;
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

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }

    @GetMapping
    public Iterable<User> all() {
        return repo.findAll();
    }
}