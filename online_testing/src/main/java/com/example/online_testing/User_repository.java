package com.example.online_testing;

import org.springframework.data.jpa.repository.JpaRepository;

public interface User_repository extends JpaRepository<User, Long> {
    boolean existsByusername (String Username);
    User findByID (Long ID);
}
