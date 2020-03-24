package com.example.demo.Repositories;

import com.example.demo.Models.Project;
import com.example.demo.Models.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VersionRepository extends JpaRepository<Version,Long> {
    Version findByID(Long Id);
    List<Version> findAllByprojectId(Project project);
}
