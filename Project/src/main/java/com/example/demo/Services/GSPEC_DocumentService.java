package com.example.demo.Services;

import com.example.demo.ErrorMessageHandling.ObjectAlreadyExistsException;
import com.example.demo.ErrorMessageHandling.ObjectNotFoundException;
import com.example.demo.Models.GSPEC_Document;
import com.example.demo.Models.Mockup;
import com.example.demo.Models.PDF_Document;
import com.example.demo.Repositories.GSPEC_DocumentRepository;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.ServisInterfaces.GSPEC_DocumentServiceInterface;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GSPEC_DocumentService implements GSPEC_DocumentServiceInterface {
    private GSPEC_DocumentRepository gspec_documentRepository;
    private MockupRepository mockupRepository;

    @Autowired
    public GSPEC_DocumentService(GSPEC_DocumentRepository gspec_documentRepository, MockupRepository mockupRepository) {
        this.gspec_documentRepository = gspec_documentRepository;
        this.mockupRepository = mockupRepository;
    }

    @Override
    public ResponseEntity addOrReplaceGSPEC(GSPEC_Document newGspec, Long id){
        GSPEC_Document gspec_document = gspec_documentRepository.findByID(id);
        if(gspec_document != null) {
            if(!newGspec.getName().equals("")) gspec_document.setName(newGspec.getName());
            if(newGspec.getMockupId() != null) gspec_document.setMockupId(newGspec.getMockupId());
            if(newGspec.getDate_created() != null) gspec_document.setDate_created(newGspec.getDate_created());
            if(newGspec.getDate_modified() != null) gspec_document.setDate_modified(newGspec.getDate_modified());
            if(newGspec.getFile() != null) gspec_document.setFile(newGspec.getFile());
            if(newGspec.getAccessed_date() != null) gspec_document.setAccessed_date(newGspec.getAccessed_date());

            gspec_documentRepository.save(gspec_document);
            return new ResponseEntity<>(gspec_document, HttpStatus.OK);
        }
        else{
            newGspec.setID(id);
            gspec_documentRepository.save(newGspec);
            return new ResponseEntity<>(newGspec, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity allGSPECsOfMockup(Long id){
        Mockup mockup = mockupRepository.findByID(id);
        if(mockup != null) {
            List<GSPEC_Document> gspec_documents = gspec_documentRepository.findAllBymockupID(mockup);
            if(gspec_documents != null){
                return new ResponseEntity<>(gspec_documents, HttpStatus.OK);
            }
            else
                throw new ObjectNotFoundException("GSPEC document with mockup with id " + id + "do not exist!");
        }
        else
            throw new ObjectNotFoundException("Mockup with id " + id + "does not exist!");
    }

    @Override
    public ResponseEntity deleteOneGSPEC(Long id) throws JSONException {
        if(gspec_documentRepository.existsById(id)){
            gspec_documentRepository.deleteById(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message","GSPEC document successfully deleted!");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        }
        else
            throw new ObjectNotFoundException("GSPEC document with id " + id + " does not exit!");
    }

    @Override
    public ResponseEntity newGSPEC(GSPEC_Document newGspec){
        List<GSPEC_Document> gspec_documents  = gspec_documentRepository.findAll();
        boolean alreadyExists = false;
        for(GSPEC_Document m: gspec_documents){
            if(m.getID().equals(newGspec.getID())) alreadyExists = true;
        }
        if(!alreadyExists) {
            GSPEC_Document gspec_document = gspec_documentRepository.save(newGspec);
            return new ResponseEntity<>(gspec_document, HttpStatus.CREATED);
        }
        else
            throw new ObjectAlreadyExistsException("GSPEC document with id " + newGspec.getID() + " already exists!");
    }

    @Override
    public ResponseEntity getAllGSPECs(){
        List<GSPEC_Document> gspec_documents = gspec_documentRepository.findAll();
        if(gspec_documents != null)
            return new ResponseEntity<>(gspec_documents, HttpStatus.OK);
        else
            throw new ObjectNotFoundException("GSPEC documents do not exist!");
    }

    @Override
    public ResponseEntity getOneGSPEC(Long id) {
        GSPEC_Document gspec_document = gspec_documentRepository.findByID(id);
        if(gspec_document != null){
            return new ResponseEntity<>(gspec_document, HttpStatus.OK);
        }
        else
        throw new ObjectNotFoundException("GSPEC document with id " + id + "does not exist!");
    }

}
