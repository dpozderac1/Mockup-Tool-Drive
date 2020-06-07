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
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            Mockup mockup = mockupRepository.findByID(pdf_document.getMockupId().getID());
            if(mockup != null) {
                if (newPdf.getMockupId() != null && mockupRepository.findByID(newPdf.getMockupId().getID()) != null) {
                    pdf_document.setMockupId(mockup);
                }

                if(!newPdf.getName().equals(" ")) pdf_document.setName(newPdf.getName());
                if(newPdf.getDate_created() != null) pdf_document.setDate_created(newPdf.getDate_created());
                if(newPdf.getDate_modified() != null) pdf_document.setDate_modified(newPdf.getDate_modified());
                if(newPdf.getFile() != null) pdf_document.setFile(newPdf.getFile());
                if(newPdf.getAccessed_date() != null) pdf_document.setAccessed_date(newPdf.getAccessed_date());

                pdf_documentRepository.save(pdf_document);
                pdf_document.setFile(null);
                mockup.setFile(null);
                return new ResponseEntity<>(pdf_document, HttpStatus.OK);
            }
            else
                throw new ObjectNotFoundException("Mockup with id " + newPdf.getMockupId().getID() + " does not exist!");
        }
        else {
            Mockup mockup = mockupRepository.findByID(newPdf.getMockupId().getID());
            if(mockup != null){
                newPdf.setMockupId(mockup);
                newPdf.setID(id);
                pdf_documentRepository.save(newPdf);
                newPdf.setFile(null);
                mockup.setFile(null);
                return new ResponseEntity<>(newPdf, HttpStatus.OK);
            }
            else
                throw new ObjectNotFoundException("Mockup with id " + newPdf.getMockupId().getID() + " does not exist!");
        }
    }

    @Override
    public ResponseEntity allPDFsOfMockup(Long id){
        Mockup mockup = mockupRepository.findByID(id);
        if(mockup != null){
            List<PDF_Document> pdf_documents = pdf_documentRepository.findAllBymockupID(mockup);
            for(PDF_Document pdf:pdf_documents){
                pdf.setFile(null);
            }
            mockup.setFile(null);
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
            jsonObject.put("message","PDF document successfully deleted!");
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
            Mockup mockup = mockupRepository.findByID(newPDF.getMockupId().getID());
            if(mockup != null) {
                newPDF.setMockupId(mockup);
                PDF_Document pdf_document = pdf_documentRepository.save(newPDF);
                pdf_document.setFile(null);
                mockup.setFile(null);
                return new ResponseEntity<>(pdf_document, HttpStatus.CREATED);
            }
            else
                throw new ObjectNotFoundException("Mockup with id " + newPDF.getMockupId().getID() + " does not exist!");
        }else
            throw new ObjectAlreadyExistsException("PDF document with id " + newPDF.getID() + " already exists!");

    }

    @Override
    public ResponseEntity getAllPDFs(){
        List<PDF_Document> pdf_documents = pdf_documentRepository.findAll();

        for(PDF_Document pdf:pdf_documents){
            pdf.getMockupId().setFile(null);
            pdf.setFile(null);
        }
        return new ResponseEntity<>(pdf_documents, HttpStatus.OK);

    }

    @Override
    public ResponseEntity getOnePDF(Long id){
        PDF_Document pdf_document = pdf_documentRepository.findByID(id);
        if(pdf_document != null) {
            pdf_document.getMockupId().setFile(null);
            pdf_document.setFile(null);
            return new ResponseEntity<>(pdf_document, HttpStatus.OK);
        }
        else
            throw new ObjectNotFoundException("PDF document with id " + id + "does not exist!");
    }

    public ResponseEntity addPDFFile(MultipartFile pdfFajl, Long id, String naziv) throws IOException, SQLException {
        //System.out.println("Usao sam u updatePDFFile");
        //System.out.println(pdfFajl.getSize());
        Mockup mockup = mockupRepository.findByID(id);
        if(mockup != null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date=new Date();
            //String datum=format.format(date);
            Blob blob=new SerialBlob(pdfFajl.getBytes());
            PDF_Document pdf=new PDF_Document(mockup,naziv,blob,date,date,date);
            pdf_documentRepository.save(pdf);

            JSONObject objekat = new JSONObject();
            try {
                objekat.put("message","PDF is successfully added!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(objekat.toString(), HttpStatus.OK);
        }
        else{
            throw new ObjectNotFoundException("Mockup with id " + id + "does not exist!");
        }
    }

    @Override
    public byte[] getOnePDFFile(Long id) throws SQLException {
        PDF_Document pdf_document = pdf_documentRepository.findByID(id);
        if(pdf_document != null) {
            Blob blob=pdf_document.getFile();
            byte[] niz=null;
            if(blob!=null){
                niz=blob.getBytes(1l, (int) blob.length());
                blob.free();
            }
            return niz;
        }
        else
            throw new ObjectNotFoundException("PDF document with id " + id + "does not exist!");
    }

}
