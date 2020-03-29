package com.example.online_testing;

import com.example.online_testing.Controllers.BrowserController;
import com.example.online_testing.Controllers.ServerController;
import com.example.online_testing.Models.Browser;
import com.example.online_testing.Models.Server;
import com.example.online_testing.Repositories.*;
import com.example.online_testing.Services.BrowserService;
import com.example.online_testing.Services.BrowserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
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
    private BrowserService browserService;

    @MockBean
    private ServerRepository serverRepository;


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
        given(browserService.getAllBrowsers()).willReturn(browsers);

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
        given(browserService.getBrowserByID(Long.valueOf(1))).willReturn(new ResponseEntity(browser, HttpStatus.OK));

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
        JSONObject jo = new JSONObject();
        jo.put("message", "Browser does not exist!");
        given(browserService.getBrowserByID(Long.valueOf(1))).willReturn(new ResponseEntity(jo.toString(), HttpStatus.NOT_FOUND));
        mvc.perform(MockMvcRequestBuilders
                .get("/browser/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Browser does not exist!"));
    }

    @Test
    public void deleteBrowserExist() throws Exception
    {
        JSONObject jo = new JSONObject();
        jo.put("message", "Browser is successfully deleted!");
        given(browserService.deleteBrowserByID(Long.valueOf(1))).willReturn(new ResponseEntity(jo.toString(), HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders.delete("/browser/{id}", 1) )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Browser is successfully deleted!"));
    }

    @Test
    public void deleteBrowserDoesNotExist() throws Exception
    {
        JSONObject jo = new JSONObject();
        jo.put("message", "Browser does not exist!");
        given(browserService.deleteBrowserByID(Long.valueOf(1))).willReturn(new ResponseEntity(jo.toString(), HttpStatus.NOT_FOUND));
        mvc.perform(MockMvcRequestBuilders.delete("/browser/{id}", 1) )
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Browser does not exist!"));
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
        given(browserService.getBrowsersServer(Long.valueOf(1))).willReturn(browsers);

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
        List<Browser> browsers = Arrays.asList();
        given(browserService.getBrowsersServer(Long.valueOf(1))).willReturn(browsers);

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

        Browser browser = new Browser("Mozila Firefox", null, "Mobile");

        JSONObject jo = new JSONObject();
        jo.put("message", "Browser is successfully added!");
        given(this.browserService.saveBrowser(ArgumentMatchers.any(Browser.class))).willReturn(new ResponseEntity(jo.toString(), HttpStatus.CREATED));

        mvc.perform(MockMvcRequestBuilders
                .post("/addBrowser")
                .content(asJsonString(browser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Browser is successfully added!"));
    }

    @Test
    public void createBrowserServerDoesNotExist() throws Exception
    {
        JSONObject jo = new JSONObject();
        jo.put("message", "Server does not exist!");
        Browser browser = new Browser("Mozila Firefox", null, "Mobile");

        given(this.browserService.saveBrowser(ArgumentMatchers.any(Browser.class))).willReturn(new ResponseEntity(jo.toString(), HttpStatus.NOT_FOUND));

        mvc.perform(MockMvcRequestBuilders
                .post("/addBrowser")
                .content(asJsonString(browser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Server does not exist!"));
    }

    @Test
    public void updateBrowserDoesExist() throws Exception
    {
        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        server.setID(Long.valueOf(1));

        Browser browser = new Browser("Mozila Firefox", 1, "Mobile");
        browser.setID(Long.valueOf(1));

        Browser browser1 = new Browser("Mozila Firefox", 1, "Tablet");

        given(this.browserService.updateBrowser(ArgumentMatchers.any(Browser.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(browser1, HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders
                .put("/updateBrowser/{id}", 1)
                .content(asJsonString(browser1))
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
        JSONObject jo = new JSONObject();
        jo.put("message", "Browser does not exist!");
        Browser browser = new Browser("Mozila Firefox", null, "Mobile");
        given(this.browserService.updateBrowser(ArgumentMatchers.any(Browser.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(jo.toString(), HttpStatus.NOT_FOUND));

        mvc.perform(MockMvcRequestBuilders
                .put("/updateBrowser/{id}", 1)
                .content(asJsonString(browser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Browser does not exist!"));
    }
}
