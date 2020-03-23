package com.example.online_testing;

import com.example.online_testing.Controllers.OnlineTestController;
import com.example.online_testing.Controllers.UserController;
import com.example.online_testing.Models.*;
import com.example.online_testing.Repositories.*;
import org.apache.commons.io.IOUtils;
import org.hibernate.event.internal.OnLockVisitor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static com.example.online_testing.BrowserControllerTests.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OnlineTestController.class)
public class OnlineTestControllerTests {

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
    public void testGetOnlineTests() throws Exception {
        OnlineTest onlineTest = new OnlineTest("Test1", null, null, null, null);
        List<OnlineTest> onlineTests = Arrays.asList(onlineTest);
        given(onlineTestRepository.findAll()).willReturn(onlineTests);

        mvc.perform(get("/onlineTests")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].tests").value(onlineTest.getTests()))
                    .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    @Test
    public void getOnlineTestByIdExist() throws Exception
    {
        OnlineTest onlineTest = new OnlineTest("Test1", null, null, null, null);
        onlineTest.setID(Long.valueOf(1));
        given(onlineTestRepository.existsByID(Long.valueOf(1))).willReturn(true);
        given(onlineTestRepository.findByID(Long.valueOf(1))).willReturn(onlineTest);

        mvc.perform(MockMvcRequestBuilders
                .get("/onlineTest/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tests").value(onlineTest.getTests()));
    }

    @Test
    public void getOnlineTestByIdDoNotExist() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                .get("/onlineTest/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Online test does not exist!"));
    }

    @Test
    public void deleteOnlineTestDoesExist() throws Exception
    {
        OnlineTest onlineTest = new OnlineTest("Test1", null, null, null, null);
        onlineTest.setID(Long.valueOf(1));
        given(onlineTestRepository.existsByID(Long.valueOf(1))).willReturn(true);
        given(onlineTestRepository.findByID(Long.valueOf(1))).willReturn(onlineTest);

        mvc.perform(MockMvcRequestBuilders.delete("/onlineTest/{id}", 1) )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Online test is successfully deleted!"));
    }

    @Test
    public void deleteOnlineTestDoesNotExist() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders.delete("/onlineTest/{id}", 1) )
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Online test does not exist!"));
    }

    @Test
    public void getOnlineTestsServerDoesExist() throws Exception
    {
        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        server.setID(Long.valueOf(1));
        given(serverRepository.findByID(Long.valueOf(1))).willReturn(server);
        OnlineTest onlineTest = new OnlineTest("Test1", null, null, null, null);
        onlineTest.setID(Long.valueOf(1));
        List<OnlineTest> onlineTests = Arrays.asList(onlineTest);
        given(onlineTestRepository.findAllByserverID(server)).willReturn(onlineTests);

        mvc.perform(MockMvcRequestBuilders
                .get("/onlineTestsServer/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tests").value(onlineTest.getTests()));
    }

    @Test
    public void getOnlineTestsServerDoesNotExist() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                .get("/onlineTestsServer/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void getOnlineTestsUsersDoesExist() throws Exception
    {
        User user = new User(null, "zramic1", "i12*67H8", "zramic1@etf.unsa.ba");
        user.setID(Long.valueOf(1));
        given(userRepository.findByID(Long.valueOf(1))).willReturn(user);
        OnlineTest onlineTest = new OnlineTest("Test1", null, null, user, null);
        onlineTest.setID(Long.valueOf(1));
        List<OnlineTest> onlineTests = Arrays.asList(onlineTest);
        given(onlineTestRepository.findAllByuserID(user)).willReturn(onlineTests);

        mvc.perform(MockMvcRequestBuilders
                .get("/onlineTestsUsers/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tests").value(onlineTest.getTests()));
    }

    @Test
    public void getOnlineTestsUsersDoesNotExist() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                .get("/onlineTestsUsers/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void getOnlineTestGSPECDocumentDoesExist() throws Exception
    {
        GSPECDocument document = new GSPECDocument("Document1", null);
        document.setID(Long.valueOf(1));
        given(gspecDocumentRepository.findByID(Long.valueOf(1))).willReturn(document);
        OnlineTest onlineTest = new OnlineTest("Test1", null, null, null, document);
        onlineTest.setID(Long.valueOf(1));
        given(onlineTestRepository.findBygspecDocumentID(document)).willReturn(onlineTest);

        mvc.perform(MockMvcRequestBuilders
                .get("/onlineTestGSPECDocument/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tests").value(onlineTest.getTests()));
    }

    @Test
    public void getOnlineTestGSPECDocumentDoesNotExist() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                .get("/onlineTestGSPECDocument/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Online test does not exist!"));
    }

    @Test
    public void createOnlineTestServerDoesExist() throws Exception
    {
        GSPECDocument document = new GSPECDocument("Document1", null);
        document.setID(Long.valueOf(1));
        given(gspecDocumentRepository.findByID(Long.valueOf(1))).willReturn(document);
        User user = new User(null, "zramic1", "i12*67H8", "zramic1@etf.unsa.ba");
        user.setID(Long.valueOf(1));
        given(userRepository.findByID(Long.valueOf(1))).willReturn(user);
        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        server.setID(Long.valueOf(1));
        given(serverRepository.findByID(Long.valueOf(1))).willReturn(server);
        given(onlineTestRepository.findBygspecDocumentID(document)).willReturn(null);

        mvc.perform(MockMvcRequestBuilders
                .post("/addOnlineTest")
                .content(asJsonString(new OnlineTest("Test1", null, 1, 1, 1)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Online test is successfully added!"));
    }

    @Test
    public void createOnlineTestServerDoesNotExist() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                .post("/addOnlineTest")
                .content(asJsonString(new OnlineTest("Test1", null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Server does not exist!"));
    }

    @Test
    public void updateOnlineTestDoesExist() throws Exception
    {
        OnlineTest onlineTest = new OnlineTest("Test1", null, 1, 1, 1);
        onlineTest.setID(Long.valueOf(1));
        given(onlineTestRepository.findByID(Long.valueOf(1))).willReturn(onlineTest);
        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        server.setID(Long.valueOf(1));
        given(serverRepository.findByID(Long.valueOf(1))).willReturn(server);
        GSPECDocument document = new GSPECDocument("Document1", null);
        document.setID(Long.valueOf(1));
        given(gspecDocumentRepository.findByID(Long.valueOf(1))).willReturn(document);
        User user = new User(null, "zramic1", "i12*67H8", "zramic1@etf.unsa.ba");
        user.setID(Long.valueOf(1));
        given(userRepository.findByID(Long.valueOf(1))).willReturn(user);

        mvc.perform(MockMvcRequestBuilders
                .put("/updateOnlineTest/{id}", 1)
                .content(asJsonString(new OnlineTest("Test2", null, 1, 1, 1)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tests").value("Test2"));
    }

    @Test
    public void updateOnlineTestDoesNotExist() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                .put("/updateOnlineTest/{id}", 2)
                .content(asJsonString(new OnlineTest("Test1", null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Online test does not exist!"));
    }

}