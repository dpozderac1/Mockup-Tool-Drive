package com.example.demo.Services;

import com.example.demo.Models.Mockup;
import com.example.demo.Models.Version;
import com.example.demo.Repositories.MockupRepository;
import com.example.demo.Repositories.VersionRepository;
import com.example.demo.ServisInterfaces.MockupServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockupService implements MockupServiceInterface{
    private MockupRepository mockupRepository;
    private VersionRepository versionRepository;

    @Autowired
    public MockupService(MockupRepository mockupRepository, VersionRepository versionRepository) {
        this.mockupRepository = mockupRepository;
        this.versionRepository = versionRepository;
    }

    @Override
    public Mockup addOrReplace(Mockup newMockup, Long id){
        Mockup mockup = mockupRepository.findByID(id);
        if(mockup != null) {
            if(!newMockup.getName().equals("")) mockup.setName(newMockup.getName());
            if(newMockup.getVersionId() != null) mockup.setVersionId(newMockup.getVersionId());
            if(newMockup.getDate_created() != null) mockup.setDate_created(newMockup.getDate_created());
            if(newMockup.getDate_modified() != null) mockup.setDate_modified(newMockup.getDate_modified());
            if(newMockup.getFile() != null) mockup.setFile(newMockup.getFile());
            if(newMockup.getAccessed_date() != null) mockup.setAccessed_date(newMockup.getAccessed_date());

            mockupRepository.save(mockup);
            return mockup;
        }
        else{
            newMockup.setID(id);
            mockupRepository.save(newMockup);
            return newMockup;
        }
    }

    @Override
    public List<Mockup> allMockupsOfVersion(Long id){
        Version version = versionRepository.findByID(id);
        return mockupRepository.findAllByversionId(version);
    }

    @Override
    public void deleteOne(Long id){
        mockupRepository.deleteById(id);
    }

    @Override
    public Mockup newMockup(Mockup newMockup){
        Mockup mockup = mockupRepository.save(newMockup);
        return mockup;
    }

    @Override
    public List<Mockup> getAllMockups(){
        return mockupRepository.findAll();
    }

    @Override
    public Mockup getOneMockup(Long id){
        return mockupRepository.findByID(id);
    }
}
