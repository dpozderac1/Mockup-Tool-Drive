package com.example.online_testing;

import com.example.online_testing.Controllers.BrowserController;
import com.example.online_testing.Controllers.ServerController;
import com.example.online_testing.Models.Browser;
import com.example.online_testing.Models.Server;
import com.example.online_testing.Repositories.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BrowserController.class)
public class BrowserControllerTests {

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

    @MockBean
    private BrowserRepository browserRepository;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetBrowsers() throws Exception {
        Browser browser = new Browser("Mozila Firefox", null, "Mobile");
        List<Browser> browsers = Arrays.asList(browser);
        given(browserRepository.findAll()).willReturn(browsers);

        mvc.perform(get("/browsers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].name").value(browser.getName()))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    @Test
    public void getBrowserByIdExist() throws Exception
    {
        Browser browser = new Browser("Mozila Firefox", null, "Mobile");
        browser.setID(Long.valueOf(1));
        given(browserRepository.existsByID(Long.valueOf(1))).willReturn(true);
        given(browserRepository.findByID(Long.valueOf(1))).willReturn(browser);

        mvc.perform(MockMvcRequestBuilders
                .get("/browser/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(browser.getName()));
    }

    @Test
    public void getBrowserByIdDoNotExist() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                .get("/browser/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Browser does not exist!"));
    }

    @Test
    public void deleteBrowserExist() throws Exception
    {
        Browser browser = new Browser("Mozila Firefox", null, "Mobile");
        browser.setID(Long.valueOf(1));
        given(browserRepository.existsByID(Long.valueOf(1))).willReturn(true);
        given(browserRepository.findByID(Long.valueOf(1))).willReturn(browser);

        mvc.perform(MockMvcRequestBuilders.delete("/browser/{id}", 1) )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Browser is successfully deleted!"));
    }

    @Test
    public void deleteBrowserDoesNotExist() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders.delete("/browser/{id}", 1) )
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Browser does not exist!"));
    }

    @Test
    public void getBrowsersServerDoesExist() throws Exception
    {
        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        server.setID(Long.valueOf(1));
        given(serverRepository.findByID(Long.valueOf(1))).willReturn(server);
        Browser browser = new Browser("Mozila Firefox", server, "Mobile");
        browser.setID(Long.valueOf(1));
        List<Browser> browsers = Arrays.asList(browser);
        given(browserRepository.findAllByserverID(server)).willReturn(browsers);

        mvc.perform(MockMvcRequestBuilders
                .get("/browsersServer/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(browser.getName()));
    }

    @Test
    public void getBrowsersServerDoesNotExist() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                .get("/browsersServer/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void createBrowserServerDoesExist() throws Exception
    {
        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        server.setID(Long.valueOf(1));
        given(serverRepository.findByID(Long.valueOf(1))).willReturn(server);

        Browser browser = new Browser("Mozila Firefox", 1, "Mobile");
        List<Browser> browsers = Arrays.asList(browser);
        given(browserRepository.findAll()).willReturn(browsers);

        mvc.perform(MockMvcRequestBuilders
                .post("/addBrowser")
                .content(asJsonString(new Browser("Mozila Firefox", 1, "Tablet")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Browser is successfully added!"));
    }

    @Test
    public void createBrowserServerDoesNotExist() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                .post("/addBrowser")
                .content(asJsonString(new Browser("Mozila Firefox", null, "Mobile")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Server does not exist!"));
    }

    @Test
    public void updateBrowserDoesExist() throws Exception
    {
        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        server.setID(Long.valueOf(1));
        given(serverRepository.findByID(Long.valueOf(1))).willReturn(server);

        Browser browser = new Browser("Mozila Firefox", 1, "Mobile");
        browser.setID(Long.valueOf(1));
        given(browserRepository.findByID(Long.valueOf(1))).willReturn(browser);

        Browser browser1 = new Browser("Google Chrome", 1, "Mobile");
        browser1.setID(Long.valueOf(2));
        List<Browser> browsers = Arrays.asList(browser1);
        given(browserRepository.findAll()).willReturn(browsers);

        mvc.perform(MockMvcRequestBuilders
                .put("/updateBrowser/{id}", 1)
                .content(asJsonString(new Browser("Mozila Firefox", 1, "Tablet")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mozila Firefox"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.idServer").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.version").value("Tablet"));
    }

    @Test
    public void updateBrowserDoesNotExist() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                .put("/updateBrowser/{id}", 2)
                .content(asJsonString(new Browser("Mozila Firefox", null, "Mobile")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Browser does not exist!"));
    }
}
