package com.example.demo.Repositories;

import com.example.demo.Models.Mockup;
import com.example.demo.Models.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MockupRepository extends JpaRepository<Mockup,Long> {
    Mockup findByID(Long ID);
    List<Mockup> findAllByversionId(Version version);
}
