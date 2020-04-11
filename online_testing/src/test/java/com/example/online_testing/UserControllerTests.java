package com.example.online_testing;

import com.example.online_testing.Controllers.UserController;
import com.example.online_testing.ErrorHandling.ApiError;
import com.example.online_testing.Models.GSPECDocument;
import com.example.online_testing.Models.Role;
import com.example.online_testing.Models.RoleNames;
import com.example.online_testing.Models.User;
import com.example.online_testing.Services.UserService;
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
import java.util.List;

import static com.example.online_testing.BrowserControllerTests.asJsonString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    public void deleteUserExists() throws Exception
    {
        JSONObject jo = new JSONObject();
        jo.put("message", "User is successfully deleted!");
        given(userService.deleteUser(Long.valueOf(1))).willReturn(new ResponseEntity(jo.toString(), HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders.delete("/deleteUser/{id}", 1) )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User is successfully deleted!"));
    }

    @Test
    public void deleteUserDoesNotExist() throws Exception
    {
        List<String> errors = new ArrayList<>();
        errors.add("User does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        given(userService.deleteUser(Long.valueOf(1))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));
        mvc.perform(MockMvcRequestBuilders.delete("/deleteUser/{id}", 1) )
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("User does not exist!"));
    }

    @Test
    public void createUserDoesExist() throws Exception
    {

        Role user = new Role(RoleNames.USER);
        user.setID(Long.valueOf(1));
        User user1 = new User("ekovac2", "ekovac2@etf.unsa.ba", 1);

        given(this.userService.saveUser(ArgumentMatchers.any(User.class))).willReturn(new ResponseEntity(user1, HttpStatus.CREATED));

        mvc.perform(MockMvcRequestBuilders
                .post("/user")
                .content(asJsonString(user1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(user1.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user1.getEmail()));
    }

    @Test
    public void createUserValidationFailed() throws Exception
    {
        List<String> errors = new ArrayList<>();
        errors.add("Email should be valid");
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation Failed", errors);
        Role user = new Role(RoleNames.USER);
        user.setID(Long.valueOf(1));
        User user1 = new User("ekovac2", "ekovac2etf.unsa.ba", 1);

        given(this.userService.saveUser(ArgumentMatchers.any(User.class))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .post("/user")
                .content(asJsonString(user1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Email should be valid"));
    }

    @Test
    public void updateUserDoesExist() throws Exception
    {
        Role user = new Role(RoleNames.USER);
        user.setID(Long.valueOf(1));
        User user1 = new User("ekovac2", "ekovac2@etf.unsa.ba", 1);

        User user2 = new User("ekovac3", "ekovac2@etf.unsa.ba", 1);

        given(this.userService.updateUser(ArgumentMatchers.anyLong(), ArgumentMatchers.any(User.class))).willReturn(new ResponseEntity(user2, HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders
                .put("/updateUser/{id}", 1)
                .content(asJsonString(user2))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(user2.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user2.getEmail()));
    }

    @Test
    public void updateUserDoesNotExist() throws Exception
    {
        List<String> errors = new ArrayList<>();
        errors.add("User does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        User user2 = new User("ekovac3", "ekovac2@etf.unsa.ba", 1);
        given(this.userService.updateUser(ArgumentMatchers.anyLong(), ArgumentMatchers.any(User.class))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .put("/updateUser/{id}", 1)
                .content(asJsonString(user2))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("User does not exist!"));
    }
}
