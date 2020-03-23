package com.example.demo;

import com.example.demo.Controllers.RoleController;
import com.example.demo.Controllers.UserController;
import com.example.demo.Models.Role;
import com.example.demo.Models.RoleNames;
import com.example.demo.Models.User;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RoleController.class)
public class RoleTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void testGetRole()
            throws Exception {

        Role uloga=new Role(RoleNames.USER);


        List<Role> uloge = Arrays.asList(uloga);

        given(roleRepository.findAll()).willReturn(uloge);

        mvc.perform(get("/roles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].role_name").value(uloga.getRole_name().toString()))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    @Test
    public void testPostRole()
            throws Exception {

        Role uloga=new Role(RoleNames.USER);

        mvc.perform(post("/role")
                .content(asJsonString(uloga))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}