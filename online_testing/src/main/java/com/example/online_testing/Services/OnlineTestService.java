package com.example.online_testing.Services;

import com.example.online_testing.Models.Browser;
import com.example.online_testing.Models.OnlineTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OnlineTestService {

    public List<OnlineTest> getAllOnlineTests();
    public ResponseEntity getOnlineTestByID(Long id);
    public ResponseEntity deleteOnlineTestByID(Long id);
    public List<OnlineTest> getOnlineTestsServers(Long id);
    public List<OnlineTest> getOnlineTestsUsers(Long id);
    public ResponseEntity getOnlineTestGSPECDocument(Long id);
    public ResponseEntity saveOnlineTest(OnlineTest onlineTest);
    public ResponseEntity updateOnlineTest(OnlineTest onlineTest, Long id);

}
