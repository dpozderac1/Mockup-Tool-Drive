package com.example.online_testing.Repositories;

import com.example.online_testing.Models.Role;
import com.example.online_testing.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByusername (String Username);
    boolean existsByID (Long ID);
    User findByID (Long ID);
    User findByusername (String Username);
    User findByroleID (Role Role_ID);
}
