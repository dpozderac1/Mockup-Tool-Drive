package com.example.demo.Repositories;

import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Models.RoleNames;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByID(Long id);
    User findByUsername(String username);
    List<User> findByroleID(Role Role_id);
    boolean existsByUsername(String username);
    boolean existsByID(Long id);
    boolean existsByRoleID(Role Role_id);
}