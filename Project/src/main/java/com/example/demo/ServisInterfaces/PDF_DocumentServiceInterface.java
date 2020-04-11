package com.example.demo.ServisInterfaces;

import com.example.demo.Models.Mockup;
import com.example.demo.Models.PDF_Document;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface PDF_DocumentServiceInterface {

    public ResponseEntity addOrReplacePDF(PDF_Document newPdf, Long id);
    public ResponseEntity allPDFsOfMockup(Long id);
    public ResponseEntity deleteOnePDF(Long id) throws JSONException;
    public ResponseEntity newPDF(PDF_Document newPDF);
    public ResponseEntity getAllPDFs();
    public ResponseEntity getOnePDF(Long id);

}
