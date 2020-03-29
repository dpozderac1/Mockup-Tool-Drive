package com.example.demo;

import com.example.demo.Controllers.RoleController;
import com.example.demo.Controllers.UserController;
import com.example.demo.Models.Role;
import com.example.demo.Models.RoleNames;
import com.example.demo.Models.User;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RoleController.class)
public class RoleTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private RoleService roleService;

    //GET /roles
    @Test
    public void testGetRoles()
            throws Exception {

        Role uloga=new Role(RoleNames.USER);

        List<Role> uloge = Arrays.asList(uloga);

        given(roleService.getAllRoles()).willReturn(uloge);

        mvc.perform(get("/roles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].role_name").value(uloga.getRole_name().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    //GET /roles/{id} DOBAR
    @Test
    public void testGetRoleByIdExists()
            throws Exception {

        Role uloga=new Role(RoleNames.USER);
        uloga.setID(Long.valueOf(1));
        ResponseEntity odgovor=new ResponseEntity(uloga, HttpStatus.OK);
        given(roleService.getRoleById(Long.valueOf(1))).willReturn(odgovor);

        mvc.perform(MockMvcRequestBuilders
                .get("/roles/{id}",1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role_name").value(uloga.getRole_name().toString()));
    }

    //GET /roles/{id} LOS
    @Test
    public void testGetRoleByIdDoesNotExist()
            throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .get("/users/{id}","nesto")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    //POST //role/{id}
    @Test
    public void testPostRole()
            throws Exception {

        Role uloga=new Role(RoleNames.USER);

        mvc.perform(post("/role")
                .content(asJsonString(uloga))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //PUT /updateRole/{id} DOBAR
    @Test
    public void testPutRoleExists()
            throws Exception {
        Role uloga=new Role(RoleNames.ADMIN);
        uloga.setID(1L);
        System.out.println("JSON je: ");
        System.out.println(asJsonString(new Role(RoleNames.USER)));

        JSONObject objekat=new JSONObject();
        objekat.put("message:","Role is successfully added!");
        Role nova=new Role(RoleNames.USER);
        given(roleService.updateRole(1L,nova)).willReturn(new ResponseEntity(objekat,HttpStatus.OK));
        mvc.perform(put("/updateRole/"+1)
                .content(asJsonString(nova))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""));
    }

    //PUT /updateRole/{id} LOS
    @Test
    public void testPutRoleDoesNotExist()
            throws Exception {
        mvc.perform(put("/updateRole/{id}","nesto")
                .content(asJsonString(new Role(RoleNames.USER)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    //DELETE /deleteRole/{id} DOBAR
    @Test
    public void testDeleteRoleExists()
            throws Exception {

        Role uloga=new Role(RoleNames.ADMIN);
        uloga.setID(Long.valueOf(1));
        given(roleRepository.findByID(Long.valueOf(1))).willReturn(uloga);

        mvc.perform(delete("/deleteRole/{id}",1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //DELETE /deleteRole/{id} LOS
    @Test
    public void testDeleteRoleDoesNotExists()
            throws Exception {
        mvc.perform(delete("/deleteRole/{id}","nesto")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
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