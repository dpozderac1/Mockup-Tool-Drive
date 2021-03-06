package com.example.demo.ServisInterfaces;

import com.example.demo.Models.PDF_Document;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

public interface PDF_DocumentServiceInterface {

    public ResponseEntity addOrReplacePDF(PDF_Document newPdf, Long id);
    public ResponseEntity allPDFsOfMockup(Long id);
    public ResponseEntity deleteOnePDF(Long id) throws JSONException;
    public ResponseEntity newPDF(PDF_Document newPDF);
    public ResponseEntity getAllPDFs();
    public ResponseEntity getOnePDF(Long id);

    public ResponseEntity addPDFFile(MultipartFile pdfFajl, Long id, String naziv) throws IOException, SQLException;

    public byte[] getOnePDFFile(Long id) throws SQLException;
}
