package com.example.demo;

import com.example.demo.Controllers.UserController;
import com.example.demo.ErrorHandling.ApiError;
import com.example.demo.Models.Project;
import com.example.demo.Models.Role;
import com.example.demo.Models.RoleNames;
import com.example.demo.Models.User;
import com.example.demo.Repositories.ProjectRepository;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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

    @MockBean
    private UserService userService;

    //GET /users
    @Test
    public void testGetUsers()
            throws Exception {

        Role uloga = new Role(RoleNames.ADMIN);
        uloga.setID(Long.valueOf(1));
        User korisnik = new User(uloga, "Zerina", "Ramic", "zramic1", "Nesto!!25", "zramic1@gmail.com");

        List<User> korisnici = Arrays.asList(korisnik);

        given(userService.getAllUsers()).willReturn(korisnici);

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

        Role uloga = new Role(RoleNames.ADMIN);
        uloga.setID(Long.valueOf(1));
        User korisnik = new User(uloga, "Zerina", "Ramic", "zramic1", "Nesto!!25", "zramic1@gmail.com");

        korisnik.setID(Long.valueOf(1));

        ResponseEntity odgovor = new ResponseEntity(korisnik, HttpStatus.OK);
        given(userService.getUserByID(Long.valueOf(1))).willReturn(odgovor);

        mvc.perform(MockMvcRequestBuilders
                .get("/users/{id}", 1)
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
                .get("/users/{id}", "nesto")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    //GET /users/role/{id} DOBAR
    @Test
    public void testGetUsersByRoleExists()
            throws Exception {

        Role uloga = new Role(RoleNames.ADMIN);
        uloga.setID(Long.valueOf(1));
        given(roleRepository.existsByID(Long.valueOf(1))).willReturn(true);
        given(roleRepository.findByID(Long.valueOf(1))).willReturn(uloga);

        User korisnik = new User(uloga, "Edina", "Kovac", "ekovac2", "Sifra22+", "ekovac2@etf.unsa.ba");
        korisnik.setID(Long.valueOf(1));

        List<User> korisnici = Arrays.asList(korisnik);

        //given(userRepository.existsByID(Long.valueOf(1))).willReturn(true);
        given(userService.getUsersByRoleID(1L)).willReturn(korisnici);

        mvc.perform(MockMvcRequestBuilders
                .get("/users/role/{id}", 1)
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
                .get("/users/role/{id}", "nesto")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }


    //GET /users/project/{id} DOBAR
    @Test
    public void testGetProjectsOfUserExists()
            throws Exception {

        Project projekat = new Project();
        projekat.setID(Long.valueOf(1));
        given(projectRepository.existsByID(Long.valueOf(1))).willReturn(true);
        given(projectRepository.findByID(Long.valueOf(1))).willReturn(projekat);

        Role uloga = new Role(RoleNames.ADMIN);
        uloga.setID(Long.valueOf(1));
        given(roleRepository.existsByID(Long.valueOf(1))).willReturn(true);
        given(roleRepository.findByID(Long.valueOf(1))).willReturn(uloga);

        User korisnik = new User(uloga, "Edina", "Kovac", "ekovac2", "Sifra22+", "ekovac2@etf.unsa.ba");
        korisnik.setID(Long.valueOf(1));
        //given(userRepository.existsByID(Long.valueOf(1))).willReturn(true);
        ResponseEntity odgovor = new ResponseEntity(korisnik, HttpStatus.OK);
        given(userService.getUserByID(ArgumentMatchers.anyLong())).willReturn(odgovor);

        List<Project> projekti = Arrays.asList(projekat);
        korisnik.setProjects(projekti);

        List<User> korisnici = Arrays.asList(korisnik);
        projekat.setUsers(korisnici);

        projekti = Arrays.asList(projekat);
        korisnik.setProjects(projekti);

        //given(userRepository.existsByID(Long.valueOf(1))).willReturn(true);
        //given(userService.getUsersByRoleID(ArgumentMatchers.anyLong())).willReturn(korisnici);
        given(userService.getUserProjects(ArgumentMatchers.anyLong())).willReturn(projekti);

        mvc.perform(MockMvcRequestBuilders
                .get("/users/projects/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(projekat.getID()));
    }

    //GET /users/projects/{id} LOS
    @Test
    public void testGetProjectsOfUserDoesNotExist()
            throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .get("/users/projects/{id}", "nesto")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }


    //POST /user
    @Test
    public void testPostUser()
            throws Exception {

        Role uloga = new Role(RoleNames.ADMIN);
        uloga.setID(Long.valueOf(1));
        given(roleRepository.findByID(1l)).willReturn(uloga);
        User korisnik = new User(uloga, "Zerina", "Ramic", "zramic1", "Nesto!!25", "zramic1@gmail.com");
        korisnik.setID(1L);
        JSONObject objekat = new JSONObject();
        objekat.put("message", "User is successfully added!");
        given(this.userService.saveUser(ArgumentMatchers.any(User.class))).willReturn(new ResponseEntity(objekat.toString(), HttpStatus.CREATED));


        mvc.perform(MockMvcRequestBuilders
                .post("/user")
                .content(asJsonString(korisnik))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User is successfully added!"));
    }

    //PUT /updateUser/{id} DOBAR
    @Test
    public void testPutUserExists()
            throws Exception {

        Role uloga = new Role(RoleNames.ADMIN);
        uloga.setID(Long.valueOf(1));
        given(roleRepository.findByID(Long.valueOf(1))).willReturn(uloga);
        User korisnik = new User(uloga, "Zerina", "Ramic", "zramic1", "Nesto!!25", "zramic1@gmail.com");
        korisnik.setID(Long.valueOf(1));

        User noviKorisnik = new User(uloga, "", "", "noviUsername", "", "");
        given(this.userService.updateUser(ArgumentMatchers.anyLong(), ArgumentMatchers.any(User.class))).willReturn(new ResponseEntity(noviKorisnik, HttpStatus.OK));

        mvc.perform(put("/updateUser/{id}", 1)
                .content(asJsonString(noviKorisnik))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(noviKorisnik.getUsername()));
    }

    //PUT /updateUser/{id} LOS
    @Test
    public void testPutUserDoesNotExist()
            throws Exception {
        mvc.perform(put("/updateUser/{id}", "nesto")
                .content(asJsonString(new User(null, "", "", "noviUsername", "", "")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    //DELETE /deleteUser/{id} DOBAR
    @Test
    public void testDeleteUserExists()
            throws Exception {

        Role uloga = new Role(RoleNames.ADMIN);
        uloga.setID(Long.valueOf(1));
        given(roleRepository.findByID(Long.valueOf(1))).willReturn(uloga);
        User korisnik = new User(uloga, "Zerina", "Ramic", "zramic1", "Nesto!!25", "zramic1@gmail.com");
        korisnik.setID(Long.valueOf(1));
        given(userRepository.findByID(Long.valueOf(1))).willReturn(korisnik);

        mvc.perform(delete("/deleteUser/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //DELETE /deleteUser/{id} LOS
    @Test
    public void testDeleteUserDoesNotExist()
            throws Exception {
        mvc.perform(delete("/deleteUser/{id}", "nesto")
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


    //Error handling
    //Error GET /users/{id} LOS
    @Test
    public void testGetUserByIdDoesNotExistErrorHandling()
            throws Exception {
        List<String> errors = new ArrayList<>();
        errors.add("User does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        given(userService.getUserByID(Long.valueOf(1))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(MockMvcRequestBuilders
                .get("/users/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("User does not exist!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Record Not Found"));
    }

    //Error PUT /updateUser/{id} LOS
    @Test
    public void testPutUserDoesNotExistErrorHandling()
            throws Exception {
        List<String> errors = new ArrayList<>();
        errors.add("User does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        Role nova = new Role(RoleNames.USER);
        nova.setID(1L);
        User korisnik = new User(null, "", "", "noviUsername", "", "");
        given(this.userService.updateUser(ArgumentMatchers.anyLong(), ArgumentMatchers.any(User.class))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(put("/updateUser/{id}", 1)
                .content(asJsonString(korisnik))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("User does not exist!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Record Not Found"));
    }

    //Error /deleteUser/{id}
    @Test
    public void testDeleteUserDoesNotExistErrorHandling()
            throws Exception {

        List<String> errors = new ArrayList<>();
        errors.add("User does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        given(userService.deleteUser(ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(MockMvcRequestBuilders
                .delete("/deleteUser/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("User does not exist!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Record Not Found"));
    }


    @Test
    public void testHttpRequestMethodNotSupported()
            throws Exception {

        List<String> errors = new ArrayList<>();
        errors.add("User does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        given(userService.getUserByID(Long.valueOf(1))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(MockMvcRequestBuilders
                .get("/deleteUser/{id}", 1)
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
                .get("/users/{id}", "nesto")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("id should be of type java.lang.Long"));
    }

    @Test
    public void testHandleAlreadyExistsException()
            throws Exception {
        List<String> errors = new ArrayList<>();
        errors.add("User already exists!");
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Record Already Exists", errors);
        Role uloga = new Role(RoleNames.USER);
        uloga.setID(1L);
        User korisnik = new User(uloga, "Zerina", "Ramic", "zramic1", "Nesto!!25", "zramic1@gmail.com");
        korisnik.setID(Long.valueOf(1));

        User noviKorisnik = new User(uloga, "Zerina", "Ramic", "zramic1", "Nesto!!25", "zramic1@gmail.com");

        given(this.userService.updateUser(ArgumentMatchers.anyLong(), ArgumentMatchers.any(User.class))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(put("/updateUser/{id}", 1)
                .content(asJsonString(noviKorisnik))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("User already exists!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Record Already Exists"));
    }

    @Test
    public void testHandleMethodArgumentNotValid() throws Exception {

        Role uloga = new Role(null);
        uloga.setID(1L);
        User korisnik = new User(uloga, "Zerina", "Ramic", "zramic1", "Nesto1*", "zramic1@gmail.com");
        korisnik.setID(1L);

        mvc.perform(MockMvcRequestBuilders
                .post("/user")
                .content(asJsonString(korisnik))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Password must be at least 8 characters long!"));
    }


    /*@Test
    public void testRestProjects()
            throws Exception {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        ResponseEntity<Project[]> response = testRestTemplate.getForEntity("http://localhost:8081/projects",Project[].class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }*/






}
