package com.example.demo;

import com.example.demo.Controllers.ProjectController;
import com.example.demo.Controllers.VersionController;
import com.example.demo.Models.Project;
import com.example.demo.Models.Version;
import com.example.demo.Models.VersionNames;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.VersionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
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

    @Test
    public void testGetVersions() throws Exception{
        Version version = new Version(null, VersionNames.DESKTOP);

        List<Version> verzije = Arrays.asList(version);
        given(versionRepository.findAll()).willReturn(verzije);

        mvc.perform(get("/versions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].projectId").value(version.getProjectId()))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", hasSize(1)));

    }

    @Test
    public void testGetVersionByID() throws Exception {
        Version version = new Version(null, VersionNames.DESKTOP);

        given(versionRepository.findByID(Long.valueOf(1))).willReturn(version);

        mvc.perform(MockMvcRequestBuilders
                .get("/version/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(version.getProjectId()));
    }

    @Test
    public void testPostVersion() throws Exception {
        Version version = new Version(null, VersionNames.DESKTOP);

        mvc.perform(MockMvcRequestBuilders
                .post("/addVersion")
                .content(asJsonString(version))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteVersion() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/delete/version/{id}", 1))
                .andExpect(status().isAccepted());
    }

    @Test
    public void updateVersion() throws Exception {
        Version version = new Version(null, VersionNames.DESKTOP);

        given(versionRepository.findByID(Long.valueOf(1))).willReturn(version);
        version.setID(Long.valueOf(1));

        mvc.perform( MockMvcRequestBuilders
                .put("/addOrUpdateVersion/{id}", 1)
                .content(asJsonString(version))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(version.getProjectId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.version_name").value(version.getVersion_name().toString().trim()));
    }

    @Test
    public void renameVersion() throws Exception {
        Version version = new Version(null, VersionNames.DESKTOP);

        given(versionRepository.findByID(Long.valueOf(1))).willReturn(version);
        version.setID(Long.valueOf(1));

        mvc.perform( MockMvcRequestBuilders
                .put("/changeVersion/{id}", 1)
                .content(asJsonString(VersionNames.DESKTOP))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.version_name").value(version.getVersion_name().toString().trim()));
    }

    @Test
    public void testGetVersionsOfProject() throws Exception{
        Project project = new Project(null, null, null, 1);
        project.setID(Long.valueOf(1));
        given(projectRepository.findByID(Long.valueOf(1))).willReturn(project);

        Version version = new Version(project, VersionNames.DESKTOP);
        version.setID(Long.valueOf(1));
        List<Version> verzije = Arrays.asList(version);
        given(versionRepository.findAllByprojectId(project)).willReturn(verzije);

        mvc.perform(MockMvcRequestBuilders
                .get("/versions/project/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].version_name").value(version.getVersion_name().toString().trim()));
    }

}
