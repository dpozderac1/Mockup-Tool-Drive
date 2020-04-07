package com.example.demo.Services;

import com.example.demo.ErrorMessageHandling.CustomRestExceptionHandler;
import com.example.demo.ErrorMessageHandling.ObjectAlreadyExistsException;
import com.example.demo.ErrorMessageHandling.ObjectNotFoundException;
import com.example.demo.Models.*;
import com.example.demo.Repositories.*;
import com.example.demo.ServisInterfaces.ProjectServiceInterface;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ProjectService implements ProjectServiceInterface {
    private ProjectRepository projectRepository;
    private VersionRepository versionRepository;
    private MockupRepository mockupRepository;
    private GSPEC_DocumentRepository gspec_documentRepository;
    private PDF_DocumentRepository pdf_documentRepository;


    @Autowired
    public ProjectService(ProjectRepository projectRepository, VersionRepository versionRepository, MockupRepository mockupRepository, GSPEC_DocumentRepository gspec_documentRepository, PDF_DocumentRepository pdf_documentRepository) {
        this.projectRepository = projectRepository;
        this.versionRepository = versionRepository;
        this.mockupRepository = mockupRepository;
        this.gspec_documentRepository = gspec_documentRepository;
        this.pdf_documentRepository = pdf_documentRepository;
    }

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity addOrReplace(Project newProject, Long id){
        Project project = projectRepository.findByID(id);
        if (project != null){
            if(!newProject.getName().isEmpty()) project.setName(newProject.getName());
            if(newProject.getDate_created() != null) project.setDate_created(newProject.getDate_created());
            if(newProject.getDate_modified() != null) project.setDate_modified(newProject.getDate_modified());
            if(!newProject.getPriority().equals("")) project.setPriority(newProject.getPriority());
            projectRepository.save(project);
            return new ResponseEntity<>(project, HttpStatus.OK);
        }
        else {
            newProject.setID(id);
            projectRepository.save(newProject);
            return new ResponseEntity<>(newProject, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity renameProject(String name, Long id){
        Project project =  projectRepository.findByID(id);
        if(project != null){
            project.setName(name);
            projectRepository.save(project);
            return new ResponseEntity<>(project, HttpStatus.OK);
        }
        else{
            throw new ObjectNotFoundException("Project with id " + id + " does not exist!");
        }
    }

    @Override
    public ResponseEntity deleteOne(Long id) throws JSONException {
        restTemplate.delete("http://user/delete/project/{id}", id);
        if(projectRepository.existsByID(id)){
            projectRepository.deleteById(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message","Project successfully deleted!");
            return new ResponseEntity(jsonObject.toString(), HttpStatus.OK);
        }
        else
            throw new ObjectNotFoundException("Project with id " + id + " does not exit!");
    }

    @Override
    public ResponseEntity newProject(Project newProject, Long id){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<Long, Project> map = new HashMap<>();
        map.put(id, newProject);
        HttpEntity<Map<Long, Project>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<Project> responseEntity = restTemplate.postForEntity("http://user/addUserProject", entity, Project.class);
        List<Project> projects  = projectRepository.findAll();
        boolean alreadyExists = false;
        for(Project p: projects){
            if(p.getID().equals(newProject.getID())) alreadyExists = true;
        }
        if(!alreadyExists){
            Project project = projectRepository.save(newProject);
            return new ResponseEntity<>(project, HttpStatus.CREATED);
        }
        else
            throw new ObjectAlreadyExistsException("Project with id " + newProject.getID() + " already exists!");
    }

    @Override
    public ResponseEntity getAllProjects(){
        List<Project> projects = projectRepository.findAll();
        if(projects != null){
            return new ResponseEntity<>(projects, HttpStatus.OK);
        }
        else
            throw new ObjectNotFoundException("Projects not found!");
    }

    @Override
    public ResponseEntity getOneProject(Long id){
        Project project = projectRepository.findByID(id);
        if(project != null){
            return new ResponseEntity<>(project, HttpStatus.OK);
        }
        else{
            throw new ObjectNotFoundException("Project with id " + id + " does not exist!");
        }
    }

    @Override
    public ResponseEntity getFilesByFilter(String filter, Long id) throws JSONException {
        HashMap<String,Object> getAllUserFiles = getAllUserFiles(id);
        for (Map.Entry<String,Object> entry : getAllUserFiles.entrySet()) {
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());

            if (filter.equals("date_modified")) {
                if(entry.getKey().equals("pdf")) {
                    List<PDF_Document> pdf_documents = (List<PDF_Document>) entry.getValue();
                    Collections.sort(pdf_documents, new Comparator<PDF_Document>() {
                        public int compare(PDF_Document p1, PDF_Document p2) {
                            return p1.getDate_modified().compareTo(p2.getDate_modified());
                        }
                    });
                    System.out.println("pdfovi:" + pdf_documents);
                    getAllUserFiles.replace("pdf",pdf_documents);
                }
                else if (entry.getKey().equals("galen")) {
                    List<GSPEC_Document> gspec_documents = (List<GSPEC_Document>) entry.getValue();
                    Collections.sort(gspec_documents, new Comparator<GSPEC_Document>() {
                        public int compare(GSPEC_Document p1, GSPEC_Document p2) {
                            return p1.getDate_modified().compareTo(p2.getDate_modified());
                        }
                    });
                    System.out.println("galenovi:" + gspec_documents);
                    getAllUserFiles.replace("galen",gspec_documents);
                }
                else if (entry.getKey().equals("html")) {
                    List<Mockup> mockups = (List<Mockup>) entry.getValue();
                    Collections.sort(mockups, new Comparator<Mockup>() {
                        public int compare(Mockup p1, Mockup p2) {
                            return p1.getDate_modified().compareTo(p2.getDate_modified());
                        }
                    });
                    System.out.println("htmlovi:" + mockups);
                    getAllUserFiles.replace("html",mockups);
                }
            }
            else if (filter.equals("date_created")) {
                if(entry.getKey().equals("pdf")) {
                    List<PDF_Document> pdf_documents = (List<PDF_Document>) entry.getValue();
                    Collections.sort(pdf_documents, new Comparator<PDF_Document>() {
                        public int compare(PDF_Document p1, PDF_Document p2) {
                            return p1.getDate_created().compareTo(p2.getDate_created());
                        }
                    });
                    System.out.println("pdfovi:" + pdf_documents);
                    getAllUserFiles.replace("pdf", pdf_documents);
                }
                else if (entry.getKey().equals("galen")) {
                    List<GSPEC_Document> gspec_documents = (List<GSPEC_Document>) entry.getValue();
                    Collections.sort(gspec_documents, new Comparator<GSPEC_Document>() {
                        public int compare(GSPEC_Document p1, GSPEC_Document p2) {
                            return p1.getDate_created().compareTo(p2.getDate_created());
                        }
                    });
                    System.out.println("galenovi:" + gspec_documents);
                    getAllUserFiles.replace("galen", gspec_documents);
                }
                else if (entry.getKey().equals("html")) {
                    List<Mockup> mockups = (List<Mockup>) entry.getValue();
                    Collections.sort(mockups, new Comparator<Mockup>() {
                        public int compare(Mockup p1, Mockup p2) {
                            return p1.getDate_created().compareTo(p2.getDate_created());
                        }
                    });
                    System.out.println("htmlovi:" + mockups);
                    getAllUserFiles.replace("html", mockups);
                }
            }
            else if (filter.equals("name")) {
                if(entry.getKey().equals("pdf")) {
                    List<PDF_Document> pdf_documents = (List<PDF_Document>) entry.getValue();
                    Collections.sort(pdf_documents, new Comparator<PDF_Document>() {
                        public int compare(PDF_Document p1, PDF_Document p2) {
                            return p1.getName().compareTo(p2.getName());
                        }
                    });
                    System.out.println("pdfovi:" + pdf_documents);
                    getAllUserFiles.replace("pdf", pdf_documents);
                }
                else if (entry.getKey().equals("galen")) {
                    List<GSPEC_Document> gspec_documents = (List<GSPEC_Document>) entry.getValue();
                    Collections.sort(gspec_documents, new Comparator<GSPEC_Document>() {
                        public int compare(GSPEC_Document p1, GSPEC_Document p2) {
                            return p1.getName().compareTo(p2.getName());
                        }
                    });
                    System.out.println("galenovi:" + gspec_documents);
                    getAllUserFiles.replace("galen", gspec_documents);
                }
                else if (entry.getKey().equals("html")) {
                    List<Mockup> mockups = (List<Mockup>) entry.getValue();
                    Collections.sort(mockups, new Comparator<Mockup>() {
                        public int compare(Mockup p1, Mockup p2) {
                            return p1.getName().compareTo(p2.getName());
                        }
                    });
                    System.out.println("htmlovi:" + mockups);
                    getAllUserFiles.replace("html", mockups);
                }
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("message", "Filter is not defined!");
                return new ResponseEntity(jsonObject.toString(), HttpStatus.OK);
            }
        }
        return new ResponseEntity(getAllUserFiles, HttpStatus.OK);
    }

    @Override
    public ResponseEntity searchFilesByName(String name, Long id){
        HashMap<String,Object> getAllUserFiles = getAllUserFiles(id);
        for (Map.Entry<String,Object> entry : getAllUserFiles.entrySet()) {
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());

            if(entry.getKey().equals("pdf")) {
                List<PDF_Document> pdf_documents = (List<PDF_Document>) entry.getValue();
                List<PDF_Document> finalPDF = new ArrayList<>();
                for (PDF_Document p: pdf_documents) {
                    if(p.getName().contains(name))
                        finalPDF.add(p);
                }
                getAllUserFiles.replace("pdf", finalPDF);
            }
            else if (entry.getKey().equals("galen")) {
                List<GSPEC_Document> gspec_documents = (List<GSPEC_Document>) entry.getValue();
                List<GSPEC_Document> finalGSPEC = new ArrayList<>();
                for (GSPEC_Document p: gspec_documents) {
                    if(p.getName().contains(name))
                        finalGSPEC.add(p);
                }
                getAllUserFiles.replace("galen", finalGSPEC);
            }
            else if (entry.getKey().equals("html")) {
                List<Mockup> mockups = (List<Mockup>) entry.getValue();
                List<Mockup> finalMockup = new ArrayList<>();
                for (Mockup p: mockups) {
                    if(p.getName().contains(name))
                        finalMockup.add(p);
                }
                getAllUserFiles.replace("html",finalMockup);
            }

        }
        return new ResponseEntity<>(getAllUserFiles, HttpStatus.OK);
    }

    @Override
    public HashMap<String,Object> getAllUserFiles(Long id) {
        ResponseEntity<Project[]> response = restTemplate.getForEntity("http://user/users/projects/" + id.toString(), Project[].class);

        List<Mockup> mockupi = new ArrayList<Mockup>();
        List<GSPEC_Document> galeni = new ArrayList<>();
        List<PDF_Document> pdfovi = new ArrayList<>();
        for (int k = 0; k < response.getBody().length; k++) {
            Project projekat = projectRepository.findByID(response.getBody()[k].getID());
            if (projekat == null) {
                throw new ObjectNotFoundException("Project with id " + id + " does not exist!");
            }

            List<Version> verzije = versionRepository.findAllByprojectId(projekat);

            List<Mockup> trenutniMockupi=new ArrayList<>();
            for (int i = 0; i < verzije.size(); i++) {
                List<Mockup> noviMockup = mockupRepository.findAllByversionId(verzije.get(i));
                for (int j = 0; j < noviMockup.size(); j++) {
                    mockupi.add(noviMockup.get(j));
                    trenutniMockupi.add(noviMockup.get(j));
                }
            }


            for (int i = 0; i < trenutniMockupi.size(); i++) {
                List<GSPEC_Document> noviGalen = gspec_documentRepository.findAllBymockupID(trenutniMockupi.get(i));
                for (int j = 0; j < noviGalen.size(); j++) {
                    galeni.add(noviGalen.get(j));
                }
            }


            for (int i = 0; i < trenutniMockupi.size(); i++) {
                List<PDF_Document> noviPDF = pdf_documentRepository.findAllBymockupID(trenutniMockupi.get(i));
                for (int j = 0; j < noviPDF.size(); j++) {
                    pdfovi.add(noviPDF.get(j));
                }
            }
        }

        HashMap<String, Object> mapa = new HashMap<>();
        mapa.put("html", mockupi);
        mapa.put("galen", galeni);
        mapa.put("pdf", pdfovi);

        return mapa;
    }

    @Override
    public HashMap<String, Object> getRecentUserFiles(Long id) {
        ResponseEntity<Project[]> response = restTemplate.getForEntity("http://user/users/projects/" + id.toString(), Project[].class);

        List<Mockup> mockupi = new ArrayList<Mockup>();
        List<GSPEC_Document> galeni = new ArrayList<>();
        List<PDF_Document> pdfovi = new ArrayList<>();
        for (int k = 0; k < response.getBody().length; k++) {
            Project projekat = projectRepository.findByID(response.getBody()[k].getID());
            if (projekat == null) {
                throw new ObjectNotFoundException("Project with id " + id + " does not exist!");
            }

            List<Version> verzije = versionRepository.findAllByprojectId(projekat);

            List<Mockup> trenutniMockupi=new ArrayList<>();
            for (int i = 0; i < verzije.size(); i++) {
                List<Mockup> noviMockup = mockupRepository.findAllByversionId(verzije.get(i));
                for (int j = 0; j < noviMockup.size(); j++) {
                    mockupi.add(noviMockup.get(j));
                    trenutniMockupi.add(noviMockup.get(j));
                }
            }


            for (int i = 0; i < trenutniMockupi.size(); i++) {
                List<GSPEC_Document> noviGalen = gspec_documentRepository.findAllBymockupID(trenutniMockupi.get(i));
                for (int j = 0; j < noviGalen.size(); j++) {
                    galeni.add(noviGalen.get(j));
                }
            }


            for (int i = 0; i < trenutniMockupi.size(); i++) {
                List<PDF_Document> noviPDF = pdf_documentRepository.findAllBymockupID(trenutniMockupi.get(i));
                for (int j = 0; j < noviPDF.size(); j++) {
                    pdfovi.add(noviPDF.get(j));
                }
            }
        }


        if(!mockupi.isEmpty()) {
            mockupi.sort((mockup, t1) -> -mockup.getAccessed_date().compareTo(t1.getAccessed_date()));
        }
        if(!galeni.isEmpty()) {
            galeni.sort((galen, t1) -> -galen.getAccessed_date().compareTo(t1.getAccessed_date()));
        }
        if(!pdfovi.isEmpty()) {
            pdfovi.sort((pdf, t1) -> -pdf.getAccessed_date().compareTo(t1.getAccessed_date()));
        }

        HashMap<String, Object> mapa = new HashMap<>();
        if(mockupi.size()!=0) {
            mapa.put("html", mockupi.get(0));
        }
        else{
            mapa.put("html", new ArrayList<Mockup>());
        }
        if(galeni.size()!=0) {
            mapa.put("galen", galeni.get(0));
        }
        else{
            mapa.put("galen", new ArrayList<GSPEC_Document>());
        }
        if(pdfovi.size()!=0) {
            mapa.put("pdf", pdfovi.get(0));
        }
        else{
            mapa.put("pdf", new ArrayList<PDF_Document>());
        }
        System.out.println("HashMap je:");
        System.out.println(mapa);
        return mapa;
    }
}
