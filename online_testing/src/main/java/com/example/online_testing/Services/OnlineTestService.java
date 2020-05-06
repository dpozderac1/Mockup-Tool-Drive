package com.example.online_testing.Services;

import com.example.online_testing.Models.OnlineTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OnlineTestService {

     List<OnlineTest> getAllOnlineTests();
     ResponseEntity getOnlineTestByID(Long id);
     ResponseEntity deleteOnlineTestByID(Long id);
     List<OnlineTest> getOnlineTestsServers(Long id);
     List<OnlineTest> getOnlineTestsUsers(Long id);
     ResponseEntity getOnlineTestGSPECDocument(Long id);
     ResponseEntity saveOnlineTest(OnlineTest onlineTest);
     ResponseEntity updateOnlineTest(OnlineTest onlineTest, Long id);

}
