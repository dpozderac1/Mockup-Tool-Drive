package com.example.demo;

import com.example.demo.Controllers.ProjectController;
import com.example.demo.ErrorHandling.ApiError;
import com.example.demo.Models.Project;
import com.example.demo.Models.Role;
import com.example.demo.Models.RoleNames;
import com.example.demo.Models.User;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.ProjectServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProjectController.class)
public class ProjectTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProjectServiceImpl projectServiceImpl;

    @Test
    public void testNewProject() throws Exception{
        Project project = new Project();
        project.setID(1L);

        Role role = new Role(RoleNames.ADMIN);
        role.setID(1L);
        User user = new User(role, "Edina", "Kovac", "ekovac2", "Nesto!!25", "ekovac2@gmail.com");
        user.setID(1L);

        given(userRepository.findByID(1L)).willReturn(user);
        given(projectServiceImpl.newProject(ArgumentMatchers.any(Project.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity<>(project, HttpStatus.CREATED));

        Map<Long, Project> map = new HashMap<>();
        map.put(1L, project);

        mvc.perform(MockMvcRequestBuilders
                .post("/addProject")
                .content(asJsonString(map))
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
    public void testDeleteProject() throws Exception {
        Project project = new Project();
        project.setID(1L);
        given(projectRepository.existsByID(Long.valueOf(1))).willReturn(true);

        mvc.perform(delete("/delete/project/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testNewProjectErrorHandling() throws Exception{
        List<String> errors = new ArrayList<>();
        errors.add("User with id 1 does not exit!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Object Not Found", errors);
        given(projectServiceImpl.newProject(ArgumentMatchers.any(Project.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        Project project = new Project();
        project.setID(1L);
        Map<Long, Project> map = new HashMap<>();
        map.put(1L, project);

        mvc.perform(MockMvcRequestBuilders
                .post("/addProject")
                .content(asJsonString(map))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("User with id 1 does not exit!"));
    }

    @Test
    public void testDeleteProjectErrorHandling() throws Exception{
        List<String> errors = new ArrayList<>();
        errors.add("Project with id 1 does not exit!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Object Not Found", errors);
        given(projectServiceImpl.deleteProject(ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .delete("/delete/project/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Project with id 1 does not exit!"));
    }
}
