package com.example.online_testing.Repositories;

import com.example.online_testing.Models.OnlineTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnlineTestRepository extends JpaRepository<OnlineTest, Long> {
    boolean existsBytests (String Tests);
    OnlineTest findByID (Long ID);
    boolean existsByID (Long ID);
}
