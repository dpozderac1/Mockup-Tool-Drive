package com.example.online_testing.Repositories;

import com.example.online_testing.Models.GSPECDocument;
import com.example.online_testing.Models.OnlineTest;
import com.example.online_testing.Models.Server;
import com.example.online_testing.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OnlineTestRepository extends JpaRepository<OnlineTest, Long> {
    boolean existsBytests (String Tests);
    OnlineTest findByID (Long ID);
    boolean existsByID (Long ID);
    List<OnlineTest> findAllByserverID(Server Server_ID);
    List<OnlineTest> findAllByuserID(User User_ID);
    OnlineTest findBygspecDocumentID(GSPECDocument GSPEC_Document_ID);
}
