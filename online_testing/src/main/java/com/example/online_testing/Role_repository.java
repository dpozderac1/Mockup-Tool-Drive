package com.example.online_testing;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Role_repository extends JpaRepository<Role, Long> {
    Role findByroleName(RoleNames Role_name);
    boolean existsByroleName (RoleNames Role_name);
}
