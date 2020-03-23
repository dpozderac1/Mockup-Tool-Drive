package com.example.online_testing;

import com.example.online_testing.Controllers.ServerController;
import com.example.online_testing.Models.*;
import com.example.online_testing.Repositories.*;
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

import static com.example.online_testing.BrowserControllerTests.asJsonString;
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
    private OnlineTestRepository onlineTestRepository;

    @MockBean
    private ServerRepository serverRepository;

    @MockBean
    private GSPECDocumentRepository gspecDocumentRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void testGetServers() throws Exception {
        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        List<Server> servers = Arrays.asList(server);
        given(serverRepository.findAll()).willReturn(servers);

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
        given(serverRepository.findByID(Long.valueOf(1))).willReturn(server);
        given(serverRepository.existsByID(Long.valueOf(1))).willReturn(true);

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
        mvc.perform(MockMvcRequestBuilders
                .get("/server/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Server does not exist!"));
    }

    @Test
    public void deleteServerDoesExist() throws Exception
    {
        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        server.setID(Long.valueOf(1));
        given(serverRepository.findByID(Long.valueOf(1))).willReturn(server);
        given(serverRepository.existsByID(Long.valueOf(1))).willReturn(true);

        mvc.perform(MockMvcRequestBuilders.delete("/server/{id}", 1) )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Server is successfully deleted!"));
    }

    @Test
    public void deleteServerDoesNotExist() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders.delete("/server/{id}", 1) )
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Server does not exist!"));
    }

    @Test
    public void getUserServersExist() throws Exception
    {
        User user = new User(null, "zramic1", "i12*67H8", "zramic1@etf.unsa.ba");
        user.setID(Long.valueOf(1));
        given(userRepository.findByID(Long.valueOf(1))).willReturn(user);
        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        server.setID(Long.valueOf(1));
        List<Server> servers = Arrays.asList(server);
        given(serverRepository.findAllByuserID(user)).willReturn(servers);

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
        given(roleRepository.findByroleName(RoleNames.ADMIN)).willReturn(admin);
        User user = new User(admin, "zramic1", "i12*67H8", "zramic1@etf.unsa.ba");
        given(userRepository.findByroleID(admin)).willReturn(user);

        mvc.perform(MockMvcRequestBuilders
                .post("/addServer")
                .content(asJsonString(new Server("http://nekiserver1.com", 3306, "1", user)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.url").value("http://nekiserver1.com"));
    }

    @Test
    public void createServerDoesNotExist() throws Exception
    {
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
        given(serverRepository.findByID(Long.valueOf(1))).willReturn(server);

        mvc.perform(MockMvcRequestBuilders
                .put("/updateServer/{id}", 1)
                .content(asJsonString(new Server("http://nekiserver2.com", 3306, "1", null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.url").value("http://nekiserver2.com"));
    }

    @Test
    public void updateServerDoesNotExist() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                .put("/updateServer/{id}", 1)
                .content(asJsonString(new Server("http://nekiserver1.com", 3306, "1", null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("The server you want to update does not exist!"));
    }
}
