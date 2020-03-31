package com.example.demo.Services;

import com.example.demo.ErrorMessageHandling.ObjectAlreadyExistsException;
import com.example.demo.ErrorMessageHandling.ObjectNotFoundException;
import com.example.demo.Models.Mockup;
import com.example.demo.Models.PDF_Document;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.Repositories.PDF_DocumentRepository;
import com.example.demo.ServisInterfaces.PDF_DocumentServiceInterface;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PDF_DocumentService implements PDF_DocumentServiceInterface {

    private PDF_DocumentRepository pdf_documentRepository;
    private MockupRepository mockupRepository;

    @Autowired
    public PDF_DocumentService(PDF_DocumentRepository pdf_documentRepository, MockupRepository mockupRepository) {
        this.pdf_documentRepository = pdf_documentRepository;
        this.mockupRepository = mockupRepository;
    }

    @Override
    public ResponseEntity addOrReplacePDF(PDF_Document newPdf, Long id){
        PDF_Document pdf_document = pdf_documentRepository.findByID(id);
        if(pdf_document != null) {
            if(!newPdf.getName().equals("")) pdf_document.setName(newPdf.getName());
            if(newPdf.getMockupId() != null) pdf_document.setMockupId(newPdf.getMockupId());
            if(newPdf.getDate_created() != null) pdf_document.setDate_created(newPdf.getDate_created());
            if(newPdf.getDate_modified() != null) pdf_document.setDate_modified(newPdf.getDate_modified());
            if(newPdf.getFile() != null) pdf_document.setFile(newPdf.getFile());
            if(newPdf.getAccessed_date() != null) pdf_document.setAccessed_date(newPdf.getAccessed_date());

            pdf_documentRepository.save(pdf_document);
            return new ResponseEntity<>(pdf_document, HttpStatus.OK);
        }
        else {
            newPdf.setID(id);
            pdf_documentRepository.save(newPdf);
            return new ResponseEntity<>(newPdf, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity allPDFsOfMockup(Long id){
        Mockup mockup = mockupRepository.findByID(id);
        if(mockup != null){
            List<PDF_Document> pdf_documents = pdf_documentRepository.findAllBymockupID(mockup);
            if(pdf_documents != null)
                return new ResponseEntity<>(pdf_documents, HttpStatus.OK);
            else
                throw new ObjectNotFoundException("PDF document with mockup with id " + id + "do not exist!");
        }
        else
            throw new ObjectNotFoundException("Mockup with id " + id + "does not exist!");
    }

    @Override
    public ResponseEntity deleteOnePDF(Long id) throws JSONException {
        if(pdf_documentRepository.existsById(id)) {
            pdf_documentRepository.deleteById(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message","Mockup successfully deleted!");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        }
        else
            throw new ObjectNotFoundException("PDF document with id " + id + " does not exit!");
    }

    @Override
    public ResponseEntity newPDF(PDF_Document newPDF){
        List<PDF_Document> pdf_documents  = pdf_documentRepository.findAll();
        boolean alreadyExists = false;
        for(PDF_Document m: pdf_documents){
            if(m.getID().equals(newPDF.getID())) alreadyExists = true;
        }
        if(!alreadyExists) {
            PDF_Document pdf_document = pdf_documentRepository.save(newPDF);
            return new ResponseEntity<>(pdf_document, HttpStatus.CREATED);
        }else
            throw new ObjectAlreadyExistsException("PDF document with id " + newPDF.getID() + " already exists!");

    }

    @Override
    public ResponseEntity getAllPDFs(){
        List<PDF_Document> pdf_documents = pdf_documentRepository.findAll();
        if(pdf_documents != null)
            return new ResponseEntity<>(pdf_documents, HttpStatus.OK);
        else
            throw new ObjectNotFoundException("PDF documents do not exist!");
    }

    @Override
    public ResponseEntity getOnePDF(Long id){
        PDF_Document pdf_document = pdf_documentRepository.findByID(id);
        if(pdf_document != null)
            return new ResponseEntity<>(pdf_document, HttpStatus.OK);
        else
            throw new ObjectNotFoundException("PDF document with id " + id + "does not exist!");
    }

}
