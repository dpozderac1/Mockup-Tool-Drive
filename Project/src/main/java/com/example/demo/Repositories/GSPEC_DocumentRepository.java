package com.example.demo.Repositories;

import com.example.demo.Models.GSPEC_Document;
import com.example.demo.Models.Mockup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GSPEC_DocumentRepository extends JpaRepository<GSPEC_Document,Long> {
    GSPEC_Document findByID(Long Id);
    List<GSPEC_Document> findAllBymockupID(Mockup mockup);
    Boolean existsByName(String name);
}
