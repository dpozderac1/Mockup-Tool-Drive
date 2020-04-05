package com.example.online_testing;

import com.example.online_testing.Controllers.ServerController;
import com.example.online_testing.ErrorHandling.ApiError;
import com.example.online_testing.Models.*;
import com.example.online_testing.Repositories.*;
import com.example.online_testing.Services.ServerService;
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

import static com.example.online_testing.BrowserControllerTests.asJsonString;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ServerController.class)
public class ServerControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ServerService serverService;

    @MockBean
    private UserRepository userRepository;


    @Test
    public void testGetServers() throws Exception {
        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        List<Server> servers = Arrays.asList(server);
        given(serverService.getAllServers()).willReturn(servers);

        mvc.perform(get("/servers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].url").value(server.getUrl()))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    @Test
    public void getServerByIdExist() throws Exception
    {
        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        server.setID(Long.valueOf(1));
        given(serverService.getServerByID(Long.valueOf(1))).willReturn(new ResponseEntity(server, HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders
                .get("/server/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.url").value(server.getUrl()));
    }

    @Test
    public void getServerByIdDoesNotExist() throws Exception
    {
        List<String> errors = new ArrayList<>();
        errors.add("Server does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        given(serverService.getServerByID(Long.valueOf(1))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .get("/server/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Server does not exist!"));
    }

    @Test
    public void deleteServerDoesExist() throws Exception
    {
        JSONObject jo = new JSONObject();
        jo.put("message", "Server is successfully deleted!");
        given(serverService.deleteServerByID(Long.valueOf(1))).willReturn(new ResponseEntity(jo.toString(), HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders.delete("/server/{id}", 1) )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Server is successfully deleted!"));
    }

    @Test
    public void deleteServerDoesNotExist() throws Exception
    {
        List<String> errors = new ArrayList<>();
        errors.add("Server does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        given(serverService.deleteServerByID(Long.valueOf(1))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders.delete("/server/{id}", 1) )
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Server does not exist!"));
    }

    @Test
    public void getUserServersExist() throws Exception
    {
        User user = new User(null, "zramic1", "zramic1@etf.unsa.ba");
        user.setID(Long.valueOf(1));
        given(userRepository.findByID(Long.valueOf(1))).willReturn(user);

        Server server = new Server("http://nekiserver1.com", 3306, "1", user);
        server.setID(Long.valueOf(1));
        List<Server> servers = Arrays.asList(server);
        given(serverService.getUserServers(user.getID())).willReturn(servers);

        mvc.perform(MockMvcRequestBuilders
                .get("/userServers/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].url").value(server.getUrl()));
    }

    @Test
    public void getUserServersDoesNotExist() throws Exception
    {
        List<Server> servers = Arrays.asList();
        given(serverService.getUserServers(Long.valueOf(1))).willReturn(servers);

        mvc.perform(MockMvcRequestBuilders
                .get("/userServers/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void createServerDoesExist() throws Exception
    {
        Role admin = new Role();
        admin.setRole_name(RoleNames.ADMIN);

        User user = new User(admin, "zramic1", "zramic1@etf.unsa.ba");
        given(userRepository.findByroleID(admin)).willReturn(user);

        Server server = new Server("http://nekiserver1.com", 3306, "1", user);
        given(this.serverService.saveServer(ArgumentMatchers.any(Server.class))).willReturn(new ResponseEntity(server, HttpStatus.CREATED));

        mvc.perform(MockMvcRequestBuilders
                .post("/addServer")
                .content(asJsonString(server))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.url").value("http://nekiserver1.com"));
    }

    @Test
    public void createServerDoesNotExist() throws Exception
    {
        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        given(this.serverService.saveServer(ArgumentMatchers.any(Server.class))).willReturn(new ResponseEntity(server, HttpStatus.CREATED));

        mvc.perform(MockMvcRequestBuilders
                .post("/addServer")
                .content(asJsonString(new Server("http://nekiserver1.com", 3306, "1", null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.url").value("http://nekiserver1.com"));
    }

    @Test
    public void updateServerDoesExist() throws Exception
    {
        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        server.setID(Long.valueOf(1));
        Server server1 = new Server("http://nekiserver2.com", 3306, "1", null);
        given(this.serverService.updateServer(ArgumentMatchers.any(Server.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(server1, HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders
                .put("/updateServer/{id}", 1)
                .content(asJsonString(server1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.url").value("http://nekiserver2.com"));
    }

    @Test
    public void updateServerDoesNotExist() throws Exception
    {
        List<String> errors = new ArrayList<>();
        errors.add("The server you want to update does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        given(this.serverService.updateServer(ArgumentMatchers.any(Server.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .put("/updateServer/{id}", 1)
                .content(asJsonString(new Server("http://nekiserver1.com", 3306, "1", null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("The server you want to update does not exist!"));
    }

    @Test
    public void handleMethodArgumentNotValid() throws Exception {

        Server server = new Server("http", 3306, "1", null);

        mvc.perform(MockMvcRequestBuilders
                .put("/updateServer/{id}", 1)
                .content(asJsonString(server))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("must be a valid URL"));
    }

    @Test
    public void handleMethodArgumentTypeMismatch() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/server/{id}", "id")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("id should be of type java.lang.Long"));
    }

    @Test
    public void handleHttpRequestMethodNotSupported() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .patch("/server/{id}", "id")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("PATCH method is not supported for this request. Supported methods are GET DELETE "));

    }

    @Test
    public void handleAlreadyExistsException() throws Exception {
        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        server.setID(Long.valueOf(1));

        Server server1 = new Server("http://nekiserver1.com", 3306, "1", null);

        List<String> errors = new ArrayList<>();
        errors.add("Server already exists!");
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Record Already Exists", errors);

        given(this.serverService.updateServer(ArgumentMatchers.any(Server.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .put("/updateServer/{id}", 1)
                .content(asJsonString(server1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Server already exists!"));
    }
}
