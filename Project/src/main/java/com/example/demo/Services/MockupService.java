package com.example.demo.Services;

import com.example.demo.ErrorMessageHandling.ObjectAlreadyExistsException;
import com.example.demo.ErrorMessageHandling.ObjectNotFoundException;
import com.example.demo.GRPCProjectService;
import com.example.demo.Models.Mockup;
import com.example.demo.Models.Project;
import com.example.demo.Models.Version;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.Repositories.VersionRepository;
import com.example.demo.ServisInterfaces.MockupServiceInterface;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MockupService implements MockupServiceInterface{
    private MockupRepository mockupRepository;
    private VersionRepository versionRepository;

    @Autowired
    GRPCProjectService grpcProjectService;

    @Autowired
    public MockupService(MockupRepository mockupRepository, VersionRepository versionRepository, GRPCProjectService grpcProjectService) {
        this.mockupRepository = mockupRepository;
        this.versionRepository = versionRepository;
        this.grpcProjectService = grpcProjectService;
    }

    @Override
    public ResponseEntity addOrReplace(Mockup newMockup, Long id){
        Mockup mockup = mockupRepository.findByID(id);
        if(mockup != null) {

            Version version = versionRepository.findByID(newMockup.getVersionId().getID());
            if(version != null){
                grpcProjectService.action("project-client-service","PUT","/addOrUpdateMockup/{id}","SUCCESS", new Timestamp(System.currentTimeMillis()));

                mockup.setVersionId(version);

                if(!newMockup.getName().equals(" "))
                    mockup.setName(newMockup.getName());
                if(newMockup.getDate_created() != null)
                    mockup.setDate_created(newMockup.getDate_created());
                if(newMockup.getDate_modified() != null)
                    mockup.setDate_modified(newMockup.getDate_modified());
                if(newMockup.getFile() != null)
                    mockup.setFile(newMockup.getFile());
                if(newMockup.getAccessed_date() != null)
                    mockup.setAccessed_date(newMockup.getAccessed_date());

                mockupRepository.save(mockup);
                mockup.setFile(null);
                return new ResponseEntity<>(mockup, HttpStatus.OK);
            }
            else{
                grpcProjectService.action("project-client-service","PUT","/addOrUpdateMockup/{id}","NOT_FOUND", new Timestamp(System.currentTimeMillis()));
                throw new ObjectNotFoundException("Version with id " + newMockup.getVersionId().getID() + " does not exist");
            }
        }
        else{
            Version version = versionRepository.findByID(newMockup.getVersionId().getID());
            if(version != null){
                grpcProjectService.action("project-client-service","PUT","/addOrUpdateMockup/{id}","SUCCESS", new Timestamp(System.currentTimeMillis()));
                newMockup.setVersionId(version);
                newMockup.setID(id);
                mockupRepository.save(newMockup);
                newMockup.setFile(null);
                return new ResponseEntity<>(newMockup, HttpStatus.OK);
            }
            else{
                grpcProjectService.action("project-client-service","PUT","/addOrUpdateMockup/{id}","NOT_FOUND", new Timestamp(System.currentTimeMillis()));
                throw new ObjectNotFoundException("Version with id " + newMockup.getVersionId().getID() + " does not exist");
            }
        }
    }

    @Override
    public ResponseEntity allMockupsOfVersion(Long id){
        Version version = versionRepository.findByID(id);
        if(version != null){
            List<Mockup> mockups = mockupRepository.findAllByversionId(version);
            for(Mockup m:mockups){
                m.setFile(null);
            }
            if(mockups != null){
                grpcProjectService.action("project-client-service","GET","/mockups/version/{id}","SUCCESS", new Timestamp(System.currentTimeMillis()));
                return new ResponseEntity<>(mockups, HttpStatus.OK);
            }
            else{
                grpcProjectService.action("project-client-service","GET","/mockups/version/{id}","NOT_FOUND", new Timestamp(System.currentTimeMillis()));
                throw new ObjectNotFoundException("Mockup with version with id " + id + "does not exist!");
            }
        }
        else{
            grpcProjectService.action("project-client-service","GET","/mockups/version/{id}","NOT_FOUND", new Timestamp(System.currentTimeMillis()));
            throw new ObjectNotFoundException("Version with id " + id + " does not exist!");
        }
    }

    @Override
    public ResponseEntity deleteOne(Long id) throws JSONException {
        if(mockupRepository.existsById(id)){
            grpcProjectService.action("project-client-service","DELETE","/delete/mockup/{id}","SUCCESS", new Timestamp(System.currentTimeMillis()));
            mockupRepository.deleteById(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message","Mockup successfully deleted!");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        }
        else{
            grpcProjectService.action("project-client-service","DELETE","/delete/mockup/{id}","NOT_FOUND", new Timestamp(System.currentTimeMillis()));
            throw new ObjectNotFoundException("Mockup with id " + id + " does not exit!");
        }
    }

    @Override
    public ResponseEntity newMockup(Mockup newMockup){
        List<Mockup> mockups  = mockupRepository.findAll();
        boolean alreadyExists = false;
        for(Mockup m: mockups){
            if(m.getID().equals(newMockup.getID())) alreadyExists = true;
        }
        if(!alreadyExists){
            Version version = versionRepository.findByID(newMockup.getVersionId().getID());
            if(version != null) {
                grpcProjectService.action("project-client-service","POST","/addMockup","SUCCESS", new Timestamp(System.currentTimeMillis()));
                newMockup.setVersionId(version);
                Mockup mockup = mockupRepository.save(newMockup);
                mockup.setFile(null);
                return new ResponseEntity<>(mockup, HttpStatus.CREATED);
            }
            else{
                grpcProjectService.action("project-client-service","POST","/addMockup","NOT_FOUND", new Timestamp(System.currentTimeMillis()));
                throw new ObjectNotFoundException("Version with id " + newMockup.getVersionId().getID() + " does not exist");
            }
        }
        else{
            grpcProjectService.action("project-client-service","POST","/addMockup","CONFLICT", new Timestamp(System.currentTimeMillis()));
            throw new ObjectAlreadyExistsException("Mockup with id " + newMockup.getID() + " already exists!");
        }
    }

    @Override
    public ResponseEntity getAllMockups(){
        grpcProjectService.action("project-client-service","GET","/mockups","SUCCESS", new Timestamp(System.currentTimeMillis()));
        List<Mockup> mockups = mockupRepository.findAll();
        if(mockups != null) {
            for(Mockup m:mockups){
                m.setFile(null);
            }
            return new ResponseEntity<>(mockups, HttpStatus.OK);
        }
        else
            throw new ObjectNotFoundException("Mockups do not exist!");
    }

    @Override
    public ResponseEntity getOneMockup(Long id){
        Mockup mockup = mockupRepository.findByID(id);
        if(mockup != null){
            mockup.setFile(null);
            grpcProjectService.action("project-client-service","GET","/mockup/{id}","SUCCESS", new Timestamp(System.currentTimeMillis()));
            return new ResponseEntity<>(mockup, HttpStatus.OK);
        }
        else{
            grpcProjectService.action("project-client-service","GET","/mockup/{id}","NOT_FOUND", new Timestamp(System.currentTimeMillis()));
            throw new ObjectNotFoundException("Mockup with id " + id + "does not exist!");
        }
    }

    @Override
    public ResponseEntity addOrUpdateFile(MultipartFile fajl, Long id) throws IOException, SQLException {
        System.out.println("Usao sam u addOrUpdateFile");
        Mockup mockup = mockupRepository.findByID(id);
        if(mockup != null){
            Blob blob=new SerialBlob(fajl.getBytes());
            mockup.setFile(blob);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date=new Date();
            mockup.setAccessed_date(date);
            mockup.setDate_modified(date);
            mockupRepository.save(mockup);

            grpcProjectService.action("project-client-service","PUT","/addOrUpdateFile/{id}","SUCCESS", new Timestamp(System.currentTimeMillis()));
            JSONObject objekat = new JSONObject();
            try {
                objekat.put("message","Mockup is successfully updated!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(objekat.toString(), HttpStatus.OK);
        }
        else{
            grpcProjectService.action("project-client-service","PUT","/addOrUpdateFile/{id}","NOT_FOUND", new Timestamp(System.currentTimeMillis()));
            throw new ObjectNotFoundException("Mockup with id " + id + "does not exist!");
        }
    }

    @Override
    public ResponseEntity getOneFile(Long id) throws SQLException {
        Mockup mockup = mockupRepository.findByID(id);
        if(mockup != null){
            Blob blob=mockup.getFile();
            String rezultat="";
            if(blob!=null){
                byte[] niz=blob.getBytes(1l, (int) blob.length());
                blob.free();
                rezultat = new String(niz, StandardCharsets.UTF_8);
            }
            grpcProjectService.action("project-client-service","GET","/mockup/file/{id}","SUCCESS", new Timestamp(System.currentTimeMillis()));
            return new ResponseEntity<>(rezultat, HttpStatus.OK);
        }
        else {
            grpcProjectService.action("project-client-service", "GET", "/mockup/file/{id}", "NOT_FOUND", new Timestamp(System.currentTimeMillis()));
            throw new ObjectNotFoundException("Mockup with id " + id + "does not exist!");
        }
    }
}
