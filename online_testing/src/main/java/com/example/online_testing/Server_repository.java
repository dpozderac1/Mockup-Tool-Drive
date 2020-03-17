package com.example.online_testing;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Server_repository extends JpaRepository<Server, Long> {
    boolean existsByurl (String URL);
    Server findByID (Long ID);
}
