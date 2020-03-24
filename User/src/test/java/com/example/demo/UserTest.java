package com.example.demo;

import com.example.demo.Controllers.UserController;
import com.example.demo.Models.Project;
import com.example.demo.Models.Role;
import com.example.demo.Models.RoleNames;
import com.example.demo.Models.User;
import com.example.demo.Repositories.ProjectRepository;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @MockBean
    private ProjectRepository projectRepository;

    //GET /users
    @Test
    public void testGetUsers()
            throws Exception {

        Role uloga=new Role(RoleNames.ADMIN);
        uloga.setID(Long.valueOf(1));
        User korisnik=new User(uloga,"Zerina","Ramic","zramic1","Nesto!!25","zramic1@gmail.com");

        List<User> korisnici = Arrays.asList(korisnik);

        given(userRepository.findAll()).willReturn(korisnici);

        mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value(korisnik.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    //GET /users/{id} DOBAR
    @Test
    public void testGetUserByIdExists()
            throws Exception {

        Role uloga=new Role(RoleNames.ADMIN);
        uloga.setID(Long.valueOf(1));
        User korisnik=new User(uloga,"Zerina","Ramic","zramic1","Nesto!!25","zramic1@gmail.com");

        korisnik.setID(Long.valueOf(1));

        given(userRepository.existsByID(Long.valueOf(1))).willReturn(true);
        given(userRepository.findByID(Long.valueOf(1))).willReturn(korisnik);

        mvc.perform(MockMvcRequestBuilders
                .get("/users/{id}",1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(korisnik.getUsername()));
    }

    //GET /users/{id} LOS
    @Test
    public void testGetUserByIdDoesNotExist()
            throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .get("/users/{id}","nesto")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    //GET /users/role/{id} DOBAR
    @Test
    public void testGetUsersByRoleExists()
            throws Exception {

        Role uloga=new Role(RoleNames.ADMIN);
        uloga.setID(Long.valueOf(1));
        given(roleRepository.existsByID(Long.valueOf(1))).willReturn(true);
        given(roleRepository.findByID(Long.valueOf(1))).willReturn(uloga);

        User korisnik=new User(uloga,"Edina","Kovac","ekovac2","Sifra22+","ekovac2@etf.unsa.ba");
        korisnik.setID(Long.valueOf(1));

        List<User> korisnici = Arrays.asList(korisnik);

        given(userRepository.existsByID(Long.valueOf(1))).willReturn(true);
        given(userRepository.findByroleID(uloga)).willReturn(korisnici);

        mvc.perform(MockMvcRequestBuilders
                .get("/users/role/{id}",1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value(korisnik.getUsername()));
    }

    //GET /users/role/{id} LOS
    @Test
    public void testGetUsersByRoleDoesNotExist()
            throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .get("/users/role/{id}","nesto")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }



    //GET /users/project/{id} DOBAR
    @Test
    public void testGetProjectsOfUserExists()
            throws Exception {

        Project projekat=new Project();
        projekat.setID(Long.valueOf(1));
        given(projectRepository.existsByID(Long.valueOf(1))).willReturn(true);
        given(projectRepository.findByID(Long.valueOf(1))).willReturn(projekat);

        Role uloga=new Role(RoleNames.ADMIN);
        uloga.setID(Long.valueOf(1));
        given(roleRepository.existsByID(Long.valueOf(1))).willReturn(true);
        given(roleRepository.findByID(Long.valueOf(1))).willReturn(uloga);

        User korisnik=new User(uloga,"Edina","Kovac","ekovac2","Sifra22+","ekovac2@etf.unsa.ba");
        korisnik.setID(Long.valueOf(1));
        given(userRepository.existsByID(Long.valueOf(1))).willReturn(true);
        given(userRepository.findByID(Long.valueOf(1))).willReturn(korisnik);

        List<Project> projekti = Arrays.asList(projekat);
        korisnik.setProjects(projekti);

        List<User> korisnici = Arrays.asList(korisnik);
        projekat.setUsers(korisnici);

        projekti = Arrays.asList(projekat);
        korisnik.setProjects(projekti);

        given(userRepository.existsByID(Long.valueOf(1))).willReturn(true);
        given(userRepository.findByroleID(uloga)).willReturn(korisnici);

        mvc.perform(MockMvcRequestBuilders
                .get("/users/projects/{id}",1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(projekat.getID()));
    }

    //GET /users/role/{id} LOS
    @Test
    public void testGetProjectsOfUserDoesNotExists()
            throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .get("/users/projects/{id}","nesto")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }


    //POST /user
    @Test
    public void testPostUser()
            throws Exception {

        Role uloga=new Role(RoleNames.ADMIN);
        uloga.setID(Long.valueOf(1));
        User korisnik=new User(uloga,"Zerina","Ramic","zramic1","Nesto!!25","zramic1@gmail.com");

        mvc.perform(post("/user")
                .content(asJsonString(korisnik))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //PUT /updateUser/{id} DOBAR
    @Test
    public void testPutUserExists()
            throws Exception {

        Role uloga=new Role(RoleNames.ADMIN);
        uloga.setID(Long.valueOf(1));
        given(roleRepository.findByID(Long.valueOf(1))).willReturn(uloga);
        User korisnik=new User(uloga,"Zerina","Ramic","zramic1","Nesto!!25","zramic1@gmail.com");
        korisnik.setID(Long.valueOf(1));
        given(userRepository.findByID(Long.valueOf(1))).willReturn(korisnik);

        mvc.perform(put("/updateUser/{id}",1)
                .content(asJsonString(new User(uloga,"","","noviUsername","","")))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(korisnik.getUsername()));
    }

    //PUT /updateUser/{id} LOS
    @Test
    public void testPutUserDoesNotExists()
            throws Exception {
        mvc.perform(put("/updateUser/{id}",1)
                .content(asJsonString(new User(null,"","","noviUsername","","")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    //DELETE /deleteUser/{id} DOBAR
    @Test
    public void testDeleteUserExists()
            throws Exception {

        Role uloga=new Role(RoleNames.ADMIN);
        uloga.setID(Long.valueOf(1));
        given(roleRepository.findByID(Long.valueOf(1))).willReturn(uloga);
        User korisnik=new User(uloga,"Zerina","Ramic","zramic1","Nesto!!25","zramic1@gmail.com");
        korisnik.setID(Long.valueOf(1));
        given(userRepository.findByID(Long.valueOf(1))).willReturn(korisnik);

        mvc.perform(delete("/deleteUser/{id}",1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //DELETE /deleteUser/{id} LOS
    @Test
    public void testDeleteUserDoesNotExists()
            throws Exception {
        mvc.perform(delete("/deleteRole/{id}",1)
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

