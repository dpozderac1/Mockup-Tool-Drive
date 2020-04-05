package com.example.online_testing.Services;

import com.example.online_testing.ErrorHandling.AlreadyExistsException;
import com.example.online_testing.ErrorHandling.RecordNotFoundException;
import com.example.online_testing.Models.GSPECDocument;
import com.example.online_testing.Models.OnlineTest;
import com.example.online_testing.Models.Server;
import com.example.online_testing.Models.User;
import com.example.online_testing.Repositories.GSPECDocumentRepository;
import com.example.online_testing.Repositories.OnlineTestRepository;
import com.example.online_testing.Repositories.ServerRepository;
import com.example.online_testing.Repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OnlineTestServiceImpl implements OnlineTestService{

    @Autowired
    private OnlineTestRepository onlineTestRepository;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GSPECDocumentRepository gspecDocumentRepository;

    @Override
    public List<OnlineTest> getAllOnlineTests() {
        return onlineTestRepository.findAll();
    }

    @Override
    public ResponseEntity getOnlineTestByID(Long id) {
        if(onlineTestRepository.existsByID(id)) {
            OnlineTest onlineTest = onlineTestRepository.findByID(id);
            return new ResponseEntity(onlineTest, HttpStatus.OK);
        }
        else {
            throw new RecordNotFoundException("Online test does not exist!");
        }
    }

    @Override
    public ResponseEntity deleteOnlineTestByID(Long id) {
        JSONObject jo = new JSONObject();
        if(onlineTestRepository.existsByID(id)) {
            onlineTestRepository.deleteById(id);
            jo.put("message", "Online test is successfully deleted!");
            return new ResponseEntity(jo.toString(), HttpStatus.OK);
        }
        throw new RecordNotFoundException("Online test does not exist!");
    }

    @Override
    public List<OnlineTest> getOnlineTestsServers(Long id) {
        Server server = serverRepository.findByID(id);
        List<OnlineTest> onlineTests = onlineTestRepository.findAllByserverID(server);
        return onlineTests;
    }

    @Override
    public List<OnlineTest> getOnlineTestsUsers(Long id) {
        User user = userRepository.findByID(id);
        List<OnlineTest> onlineTests = onlineTestRepository.findAllByuserID(user);
        return onlineTests;
    }

    @Override
    public ResponseEntity getOnlineTestGSPECDocument(Long id) {
        GSPECDocument gspecDocument = gspecDocumentRepository.findByID(id);
        OnlineTest onlineTest = onlineTestRepository.findBygspecDocumentID(gspecDocument);
        if(onlineTest == null) {
            throw new RecordNotFoundException("Online test does not exist!");
        }
        else return new ResponseEntity(onlineTest, HttpStatus.OK);
    }

    @Override
    public ResponseEntity saveOnlineTest(OnlineTest onlineTest) {
        Server server = serverRepository.findByID(Long.valueOf(onlineTest.getIdServer()));
        User user = userRepository.findByID(Long.valueOf(onlineTest.getIdUser()));
        GSPECDocument gspecDocument = gspecDocumentRepository.findByID(Long.valueOf(onlineTest.getIdGspecDocument()));
        OnlineTest onlineTest1 = new OnlineTest();
        if(server == null) {
            throw new RecordNotFoundException("Server does not exist!");
        }
        else if(user == null) {
            throw new RecordNotFoundException("User does not exist!");
        }
        else if(gspecDocument == null) {
            throw new RecordNotFoundException("GSPEC Document does not exist!");
        }
        else {
            OnlineTest onlineTest11 = onlineTestRepository.findBygspecDocumentID(gspecDocument);
            if(onlineTest11 == null){
                onlineTest1.setTests(onlineTest.getTests());
                onlineTest1.setTest_results(onlineTest.getTest_results());
                onlineTest1.setServerID(server);
                onlineTest1.setUserID(user);
                onlineTest1.setGspecDocumentID(gspecDocument);
                List<OnlineTest> onlineTests = onlineTestRepository.findAll();
                boolean postoji = false;
                for (OnlineTest ot: onlineTests) {
                    if(ot.getTests().equals(onlineTest1.getTests()) && ot.getServerID().equals(onlineTest1.getServerID()) && ot.getUserID().equals(onlineTest1.getUserID()) && ot.getGspecDocumentID().equals(onlineTest1.getGspecDocumentID()))  {
                        postoji = true;
                    }
                }
                if(!postoji) onlineTestRepository.save(onlineTest1);
                else {
                    throw new AlreadyExistsException("Online test already exists!");
                }
            }
            else {
                throw new AlreadyExistsException("Online test for this GSPEC document already exists!");
            }
        }
        return new ResponseEntity(onlineTest1, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity updateOnlineTest(OnlineTest onlineTest, Long id) {
        OnlineTest onlineTest1 = onlineTestRepository.findByID(id);
        JSONObject jo = new JSONObject();
        if(onlineTest1 == null) {
            throw new RecordNotFoundException("The online test you want to update does not exist!");
        }
        else {
            OnlineTest newOnlineTest = new OnlineTest(onlineTest1.getTests(), onlineTest1.getTest_results(), onlineTest1.getServerID(), onlineTest1.getUserID(), onlineTest1.getGspecDocumentID());
            if(!onlineTest.getTests().isEmpty()) {
                newOnlineTest.setTests(onlineTest.getTests());
            }
            if(onlineTest.getTest_results() != null) {
                newOnlineTest.setTest_results(onlineTest.getTest_results());
            }
            if(!Integer.toString(onlineTest.getIdServer()).equals(Integer.toString(0))) {
                Server server = serverRepository.findByID(Long.valueOf(onlineTest.getIdServer()));
                if (server == null) {
                    throw new RecordNotFoundException("Server does not exist!");
                }
                else {
                    newOnlineTest.setServerID(server);
                }
            }
            if(!Integer.toString(onlineTest.getIdUser()).equals(Integer.toString(0))) {
                User user = userRepository.findByID(Long.valueOf(onlineTest.getIdUser()));
                if (user == null) {
                    throw new RecordNotFoundException("User does not exist!");
                }
                else {
                    newOnlineTest.setUserID(user);
                }
            }
            if(!Integer.toString(onlineTest.getIdGspecDocument()).equals(Integer.toString(0))) {
                GSPECDocument gspecDocument = gspecDocumentRepository.findByID(Long.valueOf(onlineTest.getIdGspecDocument()));
                if (gspecDocument == null) {
                    throw new RecordNotFoundException("GSPEC Document does not exist!");
                }
                else {
                    newOnlineTest.setGspecDocumentID(gspecDocument);
                }
            }
            List<OnlineTest> onlineTests = onlineTestRepository.findAll();
            boolean postoji = false;
            for (OnlineTest ot: onlineTests) {
                if(ot.getTests().equals(newOnlineTest.getTests()) && ot.getServerID().equals(newOnlineTest.getServerID()) && ot.getUserID().equals(newOnlineTest.getUserID()) && ot.getGspecDocumentID().equals(newOnlineTest.getGspecDocumentID()))  {
                    postoji = true;
                }
            }
            if(!postoji) {
                onlineTest1.setTests(newOnlineTest.getTests());
                onlineTest1.setTest_results(newOnlineTest.getTest_results());
                onlineTest1.setServerID(newOnlineTest.getServerID());
                onlineTest1.setUserID(newOnlineTest.getUserID());
                onlineTest1.setGspecDocumentID(newOnlineTest.getGspecDocumentID());
                onlineTestRepository.save(onlineTest1);
            }
            else {
                throw new AlreadyExistsException("Online test already exists!");
            }
        }
        return new ResponseEntity(onlineTest1, HttpStatus.OK);
    }
}
