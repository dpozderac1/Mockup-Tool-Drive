package com.example.demo;

import com.example.demo.Controllers.RoleController;
import com.example.demo.Controllers.UserController;
import com.example.demo.ErrorHandling.ApiError;
import com.example.demo.Models.Role;
import com.example.demo.Models.RoleNames;
import com.example.demo.Models.User;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.asm.Advice;
import org.json.JSONObject;
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
        given(roleService.getRoleByID(Long.valueOf(1))).willReturn(odgovor);

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
                .get("/roles/{id}","nesto")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    //POST //role/{id}
    @Test
    public void testPostRole()
            throws Exception {

        Role uloga=new Role(RoleNames.USER);

        JSONObject objekat = new JSONObject();
        objekat.put("message", "Role is successfully added!");
        given(this.roleService.saveRole(ArgumentMatchers.any(Role.class))).willReturn(new ResponseEntity(objekat.toString(), HttpStatus.CREATED));

        mvc.perform(MockMvcRequestBuilders
                .post("/role")
                .content(asJsonString(uloga))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Role is successfully added!"));
    }

    //PUT /updateRole/{id} DOBAR
    @Test
    public void testPutRoleExists()
            throws Exception {
        Role uloga=new Role(RoleNames.ADMIN);
        uloga.setID(1L);

        Role nova=new Role(RoleNames.USER);
        nova.setID(1L);
        given(this.roleService.updateRole(ArgumentMatchers.anyLong(),ArgumentMatchers.any(Role.class))).willReturn(new ResponseEntity(nova,HttpStatus.OK));
        mvc.perform(put("/updateRole/{id}",1)
                .content(asJsonString(nova))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role_name").value(nova.getRole_name().toString()));
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
    public void testDeleteRoleDoesNotExist()
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







    //Error handling
    //Error GET /roles/{id} LOS
    @Test
    public void testGetRoleByIdDoesNotExistErrorHandler()
            throws Exception {
        List<String> errors = new ArrayList<>();
        errors.add("Role does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        given(roleService.getRoleByID(Long.valueOf(1))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(MockMvcRequestBuilders
                .get("/roles/{id}",1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Role does not exist!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Record Not Found"));
    }

    //Error PUT /updateRole/{id} LOS
    @Test
    public void testPutRoleDoesNotExistErrorHandler()
            throws Exception {
        List<String> errors = new ArrayList<>();
        errors.add("Role does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        Role nova=new Role(RoleNames.USER);
        nova.setID(1L);
        given(this.roleService.updateRole(ArgumentMatchers.anyLong(),ArgumentMatchers.any(Role.class))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(put("/updateRole/{id}",1)
                .content(asJsonString(nova))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Role does not exist!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Record Not Found"));
    }

    //Error DELETE /deleteRole/{id} LOS
    @Test
    public void testDeleteRoleDoesNotExistErrorHandler()
            throws Exception {

        List<String> errors = new ArrayList<>();
        errors.add("Role does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        given(roleService.deleteRole(Long.valueOf(1))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(MockMvcRequestBuilders
                .delete("/deleteRole/{id}",1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Role does not exist!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Record Not Found"));
    }


    @Test
    public void testHttpRequestMethodNotSupported()
            throws Exception {

        List<String> errors = new ArrayList<>();
        errors.add("Role does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        given(roleService.getRoleByID(Long.valueOf(1))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(MockMvcRequestBuilders
                .get("/deleteRole/{id}",1)
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
                .get("/roles/{id}","nesto")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("id should be of type java.lang.Long"));
    }

    @Test
    public void testHandleAlreadyExistsException()
            throws Exception {
        List<String> errors = new ArrayList<>();
        errors.add("Role already exists!");
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Record Already Exists", errors);
        Role uloga=new Role(RoleNames.USER);
        uloga.setID(1L);
        Role nova=new Role(RoleNames.USER);

        given(this.roleService.updateRole(ArgumentMatchers.anyLong(),ArgumentMatchers.any(Role.class))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(put("/updateRole/{id}",1)
                .content(asJsonString(nova))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Role already exists!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Record Already Exists"));
    }

}