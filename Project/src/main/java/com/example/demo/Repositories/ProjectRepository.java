package com.example.demo.Repositories;

import com.example.demo.Models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    Project findByID(Long Id);
    Boolean existsByID(Long Id);
    Boolean existsByName(String name);
}
