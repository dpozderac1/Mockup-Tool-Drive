package com.example.online_testing.Repositories;

import com.example.online_testing.Models.Browser;
import com.example.online_testing.Models.Server;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrowserRepository extends JpaRepository<Browser, Long> {
    Browser findByID(Long ID);
    boolean existsByID(Long ID);
    List<Browser> findAllByserverID(Server Server_ID);
}
