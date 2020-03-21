package com.example.online_testing.Repositories;

import com.example.online_testing.Models.Role;
import com.example.online_testing.Models.RoleNames;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByroleName(RoleNames Role_name);
    boolean existsByroleName (RoleNames Role_name);
}
