package com.example.demo;

import com.example.demo.Controllers.ProjectController;
import com.example.demo.Models.Project;
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

    @Test
    public void testGetProjects() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), 1);

        List<Project> projekti = Arrays.asList(project);
        given(projectRepository.findAll()).willReturn(projekti);

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

        given(projectRepository.findByID(Long.valueOf(1))).willReturn(project);

        mvc.perform(MockMvcRequestBuilders
                .get("/project/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(project.getName()));
    }

    @Test
    public void testPostProject() throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), 1);

        mvc.perform(MockMvcRequestBuilders
                .post("/addProject")
                .content(asJsonString(project))
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
    public void deleteProject() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/delete/project/{id}", 1))
                    .andExpect(status().isAccepted());
    }

    @Test
    public void updateProject() throws Exception
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", null, null, 1);

        given(projectRepository.findByID(Long.valueOf(1))).willReturn(project);
        project.setID(Long.valueOf(1));

        mvc.perform( MockMvcRequestBuilders
                .put("/addOrUpdateProject/{id}", 1)
                .content(asJsonString(project))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(project.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date_created").value(project.getDate_created()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date_modified").value(project.getDate_modified()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value(project.getPriority()));
    }

    @Test
    public void renameProject() throws Exception
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Project project = new Project("Mockup tool", format.parse( "2020-3-17" ), format.parse( "2020-3-17" ), 1);

        given(projectRepository.findByID(Long.valueOf(1))).willReturn(project);

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
