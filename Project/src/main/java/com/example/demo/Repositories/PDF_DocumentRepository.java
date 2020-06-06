package com.example.demo.Repositories;

import com.example.demo.Models.Mockup;
import com.example.demo.Models.PDF_Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PDF_DocumentRepository extends JpaRepository<PDF_Document,Long> {
    PDF_Document findByID(Long Id);
    List<PDF_Document> findAllBymockupID(Mockup mockup);
    Boolean existsByName(String name);
}
