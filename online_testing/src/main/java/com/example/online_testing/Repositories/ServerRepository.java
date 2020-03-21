package com.example.online_testing.Repositories;

import com.example.online_testing.Models.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Long> {
    boolean existsByurl (String URL);
    Server findByID (Long ID);
    boolean existsByID (Long ID);
}
