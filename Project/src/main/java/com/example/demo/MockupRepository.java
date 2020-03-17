package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MockupRepository extends JpaRepository<Mockup,Long> {
    Mockup findByID(Long ID);
}
