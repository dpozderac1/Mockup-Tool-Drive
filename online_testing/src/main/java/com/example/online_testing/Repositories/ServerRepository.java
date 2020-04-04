package com.example.online_testing.Repositories;

import com.example.online_testing.Models.Server;
import com.example.online_testing.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServerRepository extends JpaRepository<Server, Long> {
    boolean existsByurl (String URL);
    Server findByID (Long ID);
    boolean existsByID (Long ID);
    List<Server> findAllByuserID (User User_ID);
}
