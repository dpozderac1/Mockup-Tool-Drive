package com.example.demo;

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
@WebMvcTest(UserController.class)
public class UserTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void testGetUser()
            throws Exception {

        Role uloga=roleRepository.findByroleName(RoleNames.USER);
        User korisnik=new User(uloga,"Zerina","Ramic","zramic1","Nesto!!25","zramic1@gmail.com");

        List<User> korisnici = Arrays.asList(korisnik);

        given(userRepository.findAll()).willReturn(korisnici);

        mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].username").value(korisnik.getUsername()))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    @Test
    public void testPostUser()
            throws Exception {

        Role uloga=roleRepository.findByroleName(RoleNames.USER);
        User korisnik=new User(uloga,"Zerina","Ramic","zramic1","Nesto!!25","zramic1@gmail.com");

        mvc.perform(post("/user")
                .content(asJsonString(korisnik))
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
