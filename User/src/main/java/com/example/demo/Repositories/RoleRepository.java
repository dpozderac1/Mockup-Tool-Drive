package com.example.demo.Repositories;

import com.example.demo.Models.Role;
import com.example.demo.Models.RoleNames;
import com.example.demo.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByroleName(RoleNames Role_name);
    Role findByID(Long id);
    boolean existsByroleName(RoleNames Role_name);
    boolean existsByID(Long id);
}
