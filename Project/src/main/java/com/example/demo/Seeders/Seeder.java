package com.example.demo.Seeders;

import com.example.demo.Models.*;
import com.example.demo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class Seeder {
    private ProjectRepository projectRepository;
    private VersionRepository versionRepository;
    private MockupRepository mockupRepository;
    private PDF_DocumentRepository pdf_documentRepository;
    private GSPEC_DocumentRepository gspec_documentRepository;

    @Autowired
    public Seeder(ProjectRepository projectRepository, VersionRepository  versionRepository, MockupRepository mockupRepository, PDF_DocumentRepository pdf, GSPEC_DocumentRepository gspec) {
        this.projectRepository = projectRepository;
        this.versionRepository = versionRepository;
        this.mockupRepository = mockupRepository;
        this.pdf_documentRepository = pdf;
        this.gspec_documentRepository = gspec;
    }

    @EventListener
    public void insert_in_database (ApplicationReadyEvent event) throws ParseException {
        addProject();
        addVersion();
        addMockup();
        addPDF();
        addGSPEC();
    }

    public void addProject() throws ParseException {
        Project projekt1 = new Project();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        projekt1.setDate_created(format.parse( "2020-3-17" ));
        projekt1.setDate_modified(format.parse( "2020-3-17" ));
        projekt1.setName("Mockup tool");
        projekt1.setPriority(1);

        projectRepository.save(projekt1);

        Project projekt2 = new Project();
        projekt2.setDate_created(format.parse( "2020-3-16" ));
        projekt2.setDate_modified(format.parse( "2020-3-16" ));
        projekt2.setName("Projekat 2");
        projekt2.setPriority(1);

        projectRepository.save(projekt2);
    }

    public void addVersion(){
        Project projekat = projectRepository.findByID(Long.valueOf(1));
        Version verzija = new Version(projekat, VersionNames.DESKTOP);

        versionRepository.save(verzija);

        Version verzija2 = new Version(projekat, VersionNames.TABLET);

        versionRepository.save(verzija2);
    }

    public void addMockup() throws ParseException {
        Version verzija = versionRepository.findByID(Long.valueOf(3));
        Blob file = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Mockup mockup = new Mockup(verzija, "mockup 1", file, format.parse( "2020-3-12" ), format.parse( "2020-3-16" ), format.parse( "2020-3-16" ));

        mockupRepository.save(mockup);

        Mockup mockup1 = new Mockup(verzija, "mockup 2", file, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-16" ));
        mockupRepository.save(mockup1);

        Mockup mockup3 = new Mockup(verzija, "mockup 3", file, format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), format.parse( "2020-3-16" ));
        mockupRepository.save(mockup3);
    }

    public void addPDF() throws ParseException {
        Blob file = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Mockup mockup = mockupRepository.findByID(Long.valueOf(5));
        PDF_Document pdf = new PDF_Document(mockup, "Pdf 1", file, format.parse( "2020-3-16" ), format.parse( "2020-3-16" ), format.parse( "2020-3-16" ));

        pdf_documentRepository.save(pdf);

        PDF_Document pdf1 = new PDF_Document(mockup, "Pdf 2", file, format.parse( "2020-3-16" ), format.parse( "2020-3-16" ), format.parse( "2020-3-16" ));
        pdf_documentRepository.save(pdf1);
    }

    public void addGSPEC() throws ParseException {
        Blob file = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Mockup mockup = mockupRepository.findByID(Long.valueOf(5));
        GSPEC_Document gspec = new GSPEC_Document(mockup, "Gspec 1", file, format.parse( "2020-3-16" ), format.parse( "2020-3-16" ), format.parse( "2020-3-16" ));

        gspec_documentRepository.save(gspec);

        GSPEC_Document gspec1 = new GSPEC_Document(mockup, "Gspec 2", file, format.parse( "2020-3-16" ), format.parse( "2020-3-16" ), format.parse( "2020-3-16" ));
        gspec_documentRepository.save(gspec1);
    }

}