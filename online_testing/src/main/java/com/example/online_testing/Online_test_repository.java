package com.example.online_testing;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Online_test_repository extends JpaRepository<Online_test, Long> {
    boolean existsBytests (String Tests);
}
