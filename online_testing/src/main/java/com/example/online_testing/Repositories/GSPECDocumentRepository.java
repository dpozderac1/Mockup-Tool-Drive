package com.example.online_testing.Repositories;

import com.example.online_testing.Models.GSPECDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GSPECDocumentRepository extends JpaRepository<GSPECDocument, Long> {
    boolean existsByname(String Name);
    GSPECDocument findByID (Long ID);
    boolean existsByID(Long ID);
}
