package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepository extends JpaRepository<Version,Long> {
    Version findByID(Long Id);
}
