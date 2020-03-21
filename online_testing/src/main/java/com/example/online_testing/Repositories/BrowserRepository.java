package com.example.online_testing.Repositories;

import com.example.online_testing.Models.Browser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrowserRepository extends JpaRepository<Browser, Long> {
    Browser findByID(Long ID);
    boolean existsByID(Long ID);
}
