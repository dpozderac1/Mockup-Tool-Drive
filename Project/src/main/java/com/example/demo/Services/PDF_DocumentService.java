package com.example.demo.Services;

import com.example.demo.Models.Mockup;
import com.example.demo.Models.PDF_Document;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.Repositories.PDF_DocumentRepository;
import com.example.demo.ServisInterfaces.PDF_DocumentServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
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
    public PDF_Document addOrReplacePDF(PDF_Document newPdf, Long id){
        PDF_Document pdf_document = pdf_documentRepository.findByID(id);
        if(pdf_document != null) {
            if(!newPdf.getName().equals("")) pdf_document.setName(newPdf.getName());
            if(newPdf.getMockupId() != null) pdf_document.setMockupId(newPdf.getMockupId());
            if(newPdf.getDate_created() != null) pdf_document.setDate_created(newPdf.getDate_created());
            if(newPdf.getDate_modified() != null) pdf_document.setDate_modified(newPdf.getDate_modified());
            if(newPdf.getFile() != null) pdf_document.setFile(newPdf.getFile());
            if(newPdf.getAccessed_date() != null) pdf_document.setAccessed_date(newPdf.getAccessed_date());

            pdf_documentRepository.save(pdf_document);
            return pdf_document;
        }
        else {
            newPdf.setID(id);
            pdf_documentRepository.save(newPdf);
            return newPdf;
        }
    }

    @Override
    public List<PDF_Document> allPDFsOfMockup(Long id){
        Mockup mockup = mockupRepository.findByID(id);
        return pdf_documentRepository.findAllBymockupID(mockup);
    }

    @Override
    public void deleteOnePDF(Long id){
        pdf_documentRepository.deleteById(id);
    }

    @Override
    public PDF_Document newPDF(PDF_Document newPDF){
        PDF_Document pdf_document = pdf_documentRepository.save(newPDF);
        return pdf_document;
    }

    @Override
    public List<PDF_Document> getAllPDFs(){
        return pdf_documentRepository.findAll();
    }

    @Override
    public PDF_Document getOnePDF(Long id){
        return pdf_documentRepository.findByID(id);
    }

}
