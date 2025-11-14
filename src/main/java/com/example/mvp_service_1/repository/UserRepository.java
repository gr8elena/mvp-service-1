package com.example.mvp_service_1.repository;


import com.example.mvp_service_1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
