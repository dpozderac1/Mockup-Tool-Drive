package com.example.demo;

import com.example.demo.Controllers.ProjectController;
import com.example.demo.ErrorMessageHandling.ApiError;
import com.example.demo.Models.*;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.VersionRepository;
import com.example.demo.Services.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockingDetails;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProjectController.class)
public class ProjectTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testGetProjects() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), 1);

        List<Project> projekti = Arrays.asList(project);
        given(projectService.getAllProjects()).willReturn(new ResponseEntity<>(projekti, HttpStatus.OK));

        mvc.perform(get("/projects")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].name").value(project.getName()))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", hasSize(1)));

    }

    @Test
    public void testGetProjectByID() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), 1);

        given(projectService.getOneProject(Long.valueOf(1))).willReturn(new ResponseEntity<>(project, HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders
                .get("/project/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(project.getName()));
    }

    @Test
    public void deleteProject() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/delete/project/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostProject() throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), 1);

        given(projectService.newProject(ArgumentMatchers.any(Project.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity<>(project, HttpStatus.CREATED));

        mvc.perform(MockMvcRequestBuilders
                .post("/addProject/{id}", 1)
                .content(asJsonString(project))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(project.getName()));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void updateProject() throws Exception
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", format.parse("2020-3-17" ), format.parse("2020-3-17") , 1);
        project.setID(1L);
        given(projectService.addOrReplace(ArgumentMatchers.any(Project.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity<>(project, HttpStatus.OK));
        project.setID(Long.valueOf(1));

        mvc.perform( MockMvcRequestBuilders
                .put("/addOrUpdateProject/{id}", 1)
                .content(asJsonString(project))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(project.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date_created").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.date_modified").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value(project.getPriority()));
    }

    @Test
    public void renameProject() throws Exception
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), 1);

        given(projectService.renameProject(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity<>(project, HttpStatus.OK));

        project.setID(Long.valueOf(1));

        mvc.perform( MockMvcRequestBuilders
                .put("/renameProject/{id}", 1)
                .content(asJsonString("project"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(project.getName()));
    }

    @Test
    public void handleMethodArgumentNotValid() throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", format.parse( "2020-5-17" ), format.parse( "2020-3-17" ), 1);

        mvc.perform(MockMvcRequestBuilders
                .put("/addOrUpdateProject/{id}", 1)
                .content(asJsonString(project))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("The date should be in the past or present date!"));
    }

    @Test
    public void handleMethodArgumentTypeMismatch() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/project/{id}", "id")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("id should be of type java.lang.Long"));
    }

    @Test
    public void handleHttpRequestMethodNotSupported() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .patch("/project/{id}", "id")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("PATCH method is not supported for this request. Supported methods are GET "));

    }

    @Test
    public void handleObjectAlreadyExistsException() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), 1);
        project.setID(Long.valueOf(1));

        Project project1 = new Project("Mockup tool", format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), 1);
        project1.setID(Long.valueOf(1));

        List<String> errors = new ArrayList<>();
        errors.add("Project with id 1 already exists!");
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Object Already Exists", errors);

        given(this.projectService.newProject(ArgumentMatchers.any(Project.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .post("/addProject")
                .content(asJsonString(project1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Project with id 1 already exists!"));
    }

    @Test
    public void handleObjectNotFoundException() throws Exception {

        List<String> errors = new ArrayList<>();
        errors.add("Project with id 11 does not exit!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Object Not Found", errors);
        given(this.projectService.deleteOne(ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .delete("/delete/project/{id}", 11))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Project with id 11 does not exit!"));
    }

    //testiranje komunikacije
    //GET /allFiles/{id}
    @Test
    public void testGetAllUserFiles() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", format.parse("2020-3-17"), format.parse("2020-3-17"), 1);
        project.setID(1L);
        Version version = new Version(project, VersionNames.DESKTOP);
        version.setID(1L);
        Mockup mockup = new Mockup(version, "Mockup", null, format.parse("2020-3-17"), format.parse("2020-3-17"), format.parse("2020-3-17"));
        mockup.setID(1L);
        GSPEC_Document gspec = new GSPEC_Document(mockup, "GSPEC", null, format.parse("2020-3-17"), format.parse("2020-3-17"), format.parse("2020-3-17"));
        gspec.setID(1L);
        PDF_Document pdf = new PDF_Document(mockup, "PDF", null, format.parse("2020-3-17"), format.parse("2020-3-17"), format.parse("2020-3-17"));
        pdf.setID(1L);

        HashMap<String, Object> mapa = new HashMap<>();
        mapa.put("html", mockup);
        mapa.put("gspec", gspec);
        mapa.put("pdf", pdf);
        given(projectService.getAllUserFiles(1L)).willReturn(mapa);

        mvc.perform(get("/allFiles/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", Matchers.hasKey("html")))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", Matchers.hasKey("gspec")))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", Matchers.hasKey("pdf")));
    }

    //GET /recentFiles/{id}
    @Test
    public void testGetRecentUserFiles() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", format.parse("2020-3-17"), format.parse("2020-3-17"), 1);
        project.setID(1L);
        Version version = new Version(project, VersionNames.DESKTOP);
        version.setID(1L);
        Mockup mockup = new Mockup(version, "Mockup", null, format.parse("2020-3-17"), format.parse("2020-3-17"), format.parse("2020-3-17"));
        mockup.setID(1L);
        GSPEC_Document gspec = new GSPEC_Document(mockup, "GSPEC", null, format.parse("2020-3-17"), format.parse("2020-3-17"), format.parse("2020-3-17"));
        gspec.setID(1L);
        PDF_Document pdf = new PDF_Document(mockup, "PDF", null, format.parse("2020-3-17"), format.parse("2020-3-17"), format.parse("2020-3-17"));
        pdf.setID(1L);

        HashMap<String, Object> mapa = new HashMap<>();
        mapa.put("html", mockup);
        mapa.put("gspec", gspec);
        mapa.put("pdf", pdf);
        given(projectService.getRecentUserFiles(1L)).willReturn(mapa);

        mvc.perform(get("/recentFiles/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", Matchers.hasKey("html")))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", Matchers.hasKey("gspec")))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", Matchers.hasKey("pdf")));
    }

    @Test
    public void addProjectRestTemplate() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), 1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<Long, Project> map = new HashMap<>();
        map.put(project.getID(), project);
        HttpEntity<Map<Long, Project>> entity = new HttpEntity<>(map, headers);

        given(restTemplate.postForObject("http://user/addUserProject", entity, Project.class)).willReturn(project);
        given(projectService.newProject(ArgumentMatchers.any(Project.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity<>(project, HttpStatus.CREATED));

        mvc.perform(MockMvcRequestBuilders
                .post("/addProject/{id}", 1)
                .content(asJsonString(project))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(project.getName()));
    }


    @Test
    public void deleteProjectRestTemplate() throws Exception {
        restTemplate.delete("http://user/delete/project/{id}", 1);
        mvc.perform(MockMvcRequestBuilders.delete("/delete/project/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void filterFilesRestTemplate() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", format.parse("2020-3-17"), format.parse("2020-3-17"), 1);
        project.setID(1L);
        Version version = new Version(project, VersionNames.DESKTOP);
        version.setID(1L);
        Mockup mockup = new Mockup(version, "Mockup", null, format.parse("2020-3-17"), format.parse("2020-3-17"), format.parse("2020-3-17"));
        mockup.setID(1L);
        Mockup mockup2 = new Mockup(version, "AMockup", null, format.parse("2020-3-17"), format.parse("2020-3-17"), format.parse("2020-3-17"));
        mockup2.setID(2L);
        List<Mockup> mockups = new ArrayList<>();
        mockups.add(mockup2);
        mockups.add(mockup);
        GSPEC_Document gspec = new GSPEC_Document(mockup, "GSPEC", null, format.parse("2020-3-17"), format.parse("2020-3-17"), format.parse("2020-3-17"));
        gspec.setID(1L);
        PDF_Document pdf = new PDF_Document(mockup, "PDF", null, format.parse("2020-3-17"), format.parse("2020-3-17"), format.parse("2020-3-17"));
        pdf.setID(1L);

        HashMap<String, Object> mapa = new HashMap<>();
        mapa.put("html", mockups);
        mapa.put("gspec", gspec);
        mapa.put("pdf", pdf);
        given(projectService.getFilesByFilter(ArgumentMatchers.any(String.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity<>(mapa, HttpStatus.OK));

        mvc.perform(get("/filterFiles/{filter}/{id}", "name", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasKey("html")))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasKey("gspec")))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasKey("pdf")));

    }

    @Test
    public void searchFilesByName() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", format.parse("2020-3-17"), format.parse("2020-3-17"), 1);
        project.setID(1L);
        Version version = new Version(project, VersionNames.DESKTOP);
        version.setID(1L);
        Mockup mockup = new Mockup(version, "Mockup", null, format.parse("2020-3-17"), format.parse("2020-3-17"), format.parse("2020-3-17"));
        mockup.setID(1L);
        GSPEC_Document gspec = new GSPEC_Document(mockup, "GSPEC", null, format.parse("2020-3-17"), format.parse("2020-3-17"), format.parse("2020-3-17"));
        gspec.setID(1L);
        PDF_Document pdf = new PDF_Document(mockup, "PDF", null, format.parse("2020-3-17"), format.parse("2020-3-17"), format.parse("2020-3-17"));
        pdf.setID(1L);

        HashMap<String, Object> mapa = new HashMap<>();
        mapa.put("html", new ArrayList<>());
        mapa.put("gspec", new ArrayList<>());
        mapa.put("pdf", pdf);
        given(projectService.searchFilesByName(ArgumentMatchers.any(String.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity<>(mapa, HttpStatus.OK));

        mvc.perform(get("/searchFilesByName/{name}/{id}", "PDF", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasKey("html")))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasKey("gspec")))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasKey("pdf")));

    }


}
