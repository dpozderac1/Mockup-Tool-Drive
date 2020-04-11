package com.example.online_testing.Services;

import com.example.online_testing.ErrorHandling.AlreadyExistsException;
import com.example.online_testing.ErrorHandling.RecordNotFoundException;
import com.example.online_testing.Models.GSPECDocument;
import com.example.online_testing.Repositories.GSPECDocumentRepository;
import com.netflix.discovery.converters.Auto;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GSPECDocumentServiceImpl implements GSPECDocumentService {


    @Autowired
    private GSPECDocumentRepository gspecDocumentRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity deleteGSPECDocumentByID(Long id) {
        JSONObject jo = new JSONObject();
        if(gspecDocumentRepository.existsByID(id)) {
            gspecDocumentRepository.deleteById(id);
            jo.put("message", "GSPEC Document is successfully deleted!");
            return new ResponseEntity(jo.toString(), HttpStatus.OK);
        }
        throw new RecordNotFoundException("GSPEC Document does not exist!");
    }

    @Override
    public ResponseEntity updateGSPECDocument(GSPECDocument gspecDocument, Long id) {
        GSPECDocument oldGspecDocument = gspecDocumentRepository.findByID(id);
        JSONObject jo = new JSONObject();
        if(oldGspecDocument == null) {
            throw new RecordNotFoundException("The GSPEC Document you want to update does not exist!");
        }
        else {
            GSPECDocument newGspecDocument = new GSPECDocument(oldGspecDocument.getName(), oldGspecDocument.getFile());
            if(!gspecDocument.getName().isEmpty()) {
                newGspecDocument.setName(gspecDocument.getName());
            }
            if(gspecDocument.getFile() != null) {
                newGspecDocument.setFile(gspecDocument.getFile());
            }
            List<GSPECDocument> gspecDocuments = gspecDocumentRepository.findAll();
            boolean postoji = false;
            for (GSPECDocument g: gspecDocuments) {
                if(g.getName().equals(newGspecDocument.getName()) && g.getFile() != null && newGspecDocument.getFile() != null && (g.getFile() == newGspecDocument.getFile()))  {
                    postoji = true;
                }
            }
            if(!postoji) {
                oldGspecDocument.setName(newGspecDocument.getName());
                oldGspecDocument.setFile(newGspecDocument.getFile());
                gspecDocumentRepository.save(oldGspecDocument);
            }
            else {
                throw new AlreadyExistsException("GSPEC DOcument already exists!");
            }
        }
        return new ResponseEntity(oldGspecDocument, HttpStatus.OK);
    }

    @Override
    public ResponseEntity saveGSPECDocument(GSPECDocument gspecDocument) {
        JSONObject jo = new JSONObject();
        GSPECDocument newGspecDocument = new GSPECDocument(gspecDocument.getName(), gspecDocument.getFile());
        List<GSPECDocument> gspecDocuments = gspecDocumentRepository.findAll();
        boolean postoji = false;
        for (GSPECDocument g: gspecDocuments) {
            if(g.getName().equals(newGspecDocument.getName()) && g.getFile() != null && newGspecDocument.getFile() != null && (g.getFile() == newGspecDocument.getFile()))  {
                postoji = true;
            }
        }
        if(!postoji) gspecDocumentRepository.save(newGspecDocument);
        else {
            throw new AlreadyExistsException("GSPEC Document already exists!");
        }
        jo.put("message", "GSPEC Document is successfully added!");
        return new ResponseEntity(newGspecDocument, HttpStatus.CREATED);
    }
}
