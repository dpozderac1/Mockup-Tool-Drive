package com.example.demo.ServisInterfaces;

import com.example.demo.Models.Mockup;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface MockupServiceInterface {
    public Mockup addOrReplace(Mockup newMockup, Long id);
    public List<Mockup> allMockupsOfVersion(Long id);
    public void deleteOne(Long id);
    public Mockup newMockup(Mockup newMockup);
    public List<Mockup> getAllMockups();
    public Mockup getOneMockup(Long id);
}
