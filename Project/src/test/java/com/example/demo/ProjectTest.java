package com.example.demo;

import com.example.demo.Controllers.ProjectController;
import com.example.demo.Models.Project;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.VersionRepository;
import com.example.demo.Services.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
                .andExpect(status().isAccepted());
    }

    @Test
    public void testPostProject() throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), 1);

        given(projectService.newProject(ArgumentMatchers.any(Project.class))).willReturn(new ResponseEntity<>(project, HttpStatus.CREATED));

        mvc.perform(MockMvcRequestBuilders
                .post("/addProject")
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

}
