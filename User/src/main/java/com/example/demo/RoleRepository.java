package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByroleName(RoleNames Role_name);
    boolean existsByroleName(RoleNames Role_name);
}
