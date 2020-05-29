package com.example.demo;

import com.example.demo.Controllers.ProjectController;
import com.example.demo.Controllers.VersionController;
import com.example.demo.ErrorMessageHandling.ApiError;
import com.example.demo.Models.Project;
import com.example.demo.Models.Version;
import com.example.demo.Models.VersionNames;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.VersionRepository;
import com.example.demo.Services.VersionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(VersionController.class)
public class VersionTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VersionRepository versionRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private VersionService versionService;

    @Test
    public void testGetVersions() throws Exception{
        Version version = new Version(null, VersionNames.DESKTOP);

        List<Version> versions = Arrays.asList(version);
        given(versionService.getAllVersions()).willReturn(new ResponseEntity<>(versions, HttpStatus.OK));

        mvc.perform(get("/versions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].projectId").value(version.getProjectId()))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", hasSize(1)));

    }

    @Test
    public void testGetVersionByID() throws Exception {
        Version version = new Version(null, VersionNames.DESKTOP);

        given(versionService.getOneVersion(1L)).willReturn(new ResponseEntity<>(version, HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders
                .get("/version/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(version.getProjectId()));
    }

    @Test
    public void testGetVersionsOfProject() throws Exception{
        Project project = new Project(null, null, null, 1);
        project.setID(1L);

        Version version = new Version(project, VersionNames.DESKTOP);
        version.setID(1L);
        List<Version> versions = Arrays.asList(version);
        given(versionService.allVersionsOfProject(1L)).willReturn(new ResponseEntity<>(versions, HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders
                .get("/versions/project/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].version_name").value(version.getVersionName().toString().trim()));
    }

    @Test
    public void deleteVersion() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/delete/version/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostVersion() throws Exception {
        Version version = new Version(null, VersionNames.DESKTOP);

        given(versionService.newVersion(ArgumentMatchers.any(Version.class))).willReturn(new ResponseEntity<>(version, HttpStatus.CREATED));
        mvc.perform(MockMvcRequestBuilders
                .post("/addVersion")
                .content(asJsonString(version))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(version.getProjectId()));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void updateVersion() throws Exception {
        Version version = new Version(null, VersionNames.DESKTOP);

        given(versionService.addOrReplace(ArgumentMatchers.any(Version.class),ArgumentMatchers.anyLong())).willReturn(new ResponseEntity<>(version, HttpStatus.OK));
        version.setID(Long.valueOf(1));

        mvc.perform( MockMvcRequestBuilders
                .put("/addOrUpdateVersion/{id}", 1)
                .content(asJsonString(version))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(version.getProjectId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.version_name").value(version.getVersionName().toString().trim()));
    }

    @Test
    public void renameVersion() throws Exception {
        Version version = new Version(null, VersionNames.DESKTOP);

        given(versionService.changeVersion(ArgumentMatchers.any(VersionNames.class),ArgumentMatchers.anyLong())).willReturn(new ResponseEntity<>(version, HttpStatus.OK));
        version.setID(Long.valueOf(1));

        mvc.perform( MockMvcRequestBuilders
                .put("/changeVersion/{id}", 1)
                .content(asJsonString(VersionNames.DESKTOP))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.version_name").value(version.getVersionName().toString().trim()));
    }


    //Error handling
    //Error GET /version/{id} LOS
    @Test
    public void testGetVersionByIdDoesNotExistErrorHandling()
            throws Exception {
        List<String> errors = new ArrayList<>();
        errors.add("Version with id 1 does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Object Not Found", errors);
        given(versionService.getOneVersion(Long.valueOf(1))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(MockMvcRequestBuilders
                .get("/version/{id}",1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Version with id 1 does not exist!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Object Not Found"));
    }

    //Error /delete/mockup/{id}
    @Test
    public void testDeleteVersionDoesNotExistErrorHandling()
            throws Exception {

        List<String> errors = new ArrayList<>();
        errors.add("Version with id 1 does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Object Not Found", errors);
        given(versionService.deleteOne(ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(MockMvcRequestBuilders
                .delete("/delete/version/{id}",1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Version with id 1 does not exist!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Object Not Found"));
    }

    @Test
    public void testHttpRequestMethodNotSupported()
            throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .get("/delete/version/{id}",1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("GET method is not supported for this request. Supported methods are DELETE "))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Request method 'GET' not supported"));
    }

    @Test
    public void testMethodArgumentTypeMismatch()
            throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/version/{id}","nesto")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("id should be of type java.lang.Long"));
    }

    @Test
    public void testHandleAlreadyExistsException()
            throws Exception {
        Version version = new Version(null, VersionNames.DESKTOP);
        version.setID(1L);

        Version version1 = new Version(null, VersionNames.DESKTOP);
        version1.setID(1L);

        List<String> errors = new ArrayList<>();
        errors.add("Version with id 1 already exists!");
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Object Already Exists", errors);

        given(this.versionService.newVersion(ArgumentMatchers.any(Version.class))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(MockMvcRequestBuilders
                .post("/addVersion",1)
                .content(asJsonString(version1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Version with id 1 already exists!"));
    }

    @Test
    public void testHandleMethodArgumentNotValid() throws Exception {

        Version version = new Version(null, null);
        version.setID(1L);

        mvc.perform(MockMvcRequestBuilders
                .put("/addOrUpdateVersion/{id}", 1)
                .content(asJsonString(version))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("must not be null"));
    }

}
