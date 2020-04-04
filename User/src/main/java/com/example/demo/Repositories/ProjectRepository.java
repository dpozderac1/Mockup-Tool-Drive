package com.example.demo.Repositories;

import com.example.demo.Models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    Project findByID(Long id);
    boolean existsByID(Long id);
}
