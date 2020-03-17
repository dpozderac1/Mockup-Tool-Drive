package com.example.online_testing;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GSPEC_Document_repository extends JpaRepository<GSPEC_Document, Long> {
    boolean existsByname(String Name);
    GSPEC_Document findByID (Long ID);
}
