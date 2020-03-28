package com.example.demo.Services;

import com.example.demo.Models.GSPEC_Document;
import com.example.demo.Models.Mockup;
import com.example.demo.Repositories.GSPEC_DocumentRepository;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.ServisInterfaces.GSPEC_DocumentServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
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
    public GSPEC_Document addOrReplaceGSPEC(GSPEC_Document newGspec, Long id){
        GSPEC_Document gspec_document = gspec_documentRepository.findByID(id);
        if(gspec_document != null) {
            if(!newGspec.getName().equals("")) gspec_document.setName(newGspec.getName());
            if(newGspec.getMockupId() != null) gspec_document.setMockupId(newGspec.getMockupId());
            if(newGspec.getDate_created() != null) gspec_document.setDate_created(newGspec.getDate_created());
            if(newGspec.getDate_modified() != null) gspec_document.setDate_modified(newGspec.getDate_modified());
            if(newGspec.getFile() != null) gspec_document.setFile(newGspec.getFile());
            if(newGspec.getAccessed_date() != null) gspec_document.setAccessed_date(newGspec.getAccessed_date());

            gspec_documentRepository.save(gspec_document);
            return gspec_document;
        }
        else{
            newGspec.setID(id);
            gspec_documentRepository.save(newGspec);
            return newGspec;
        }
    }

    @Override
    public List<GSPEC_Document> allGSPECsOfMockup(Long id){
        Mockup mockup = mockupRepository.findByID(id);
        return gspec_documentRepository.findAllBymockupID(mockup);
    }

    @Override
    public void deleteOneGSPEC(Long id){
        gspec_documentRepository.deleteById(id);
    }

    @Override
    public GSPEC_Document newGSPEC(GSPEC_Document newGspec){
        GSPEC_Document gspec_document = gspec_documentRepository.save(newGspec);
        return gspec_document;
    }

    @Override
    public List<GSPEC_Document> getAllGSPECs(){
        return gspec_documentRepository.findAll();
    }

    @Override
    public GSPEC_Document getOneGSPEC(Long id){
        return gspec_documentRepository.findByID(id);
    }

}
