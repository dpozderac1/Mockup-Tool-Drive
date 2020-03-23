package com.example.demo.Repositories;

import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByid(Long id);
    User findByUsername(String username);
    boolean existsByUsername(String username);
}
