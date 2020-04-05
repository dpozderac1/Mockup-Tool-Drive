package com.example.online_testing;

import com.example.online_testing.Controllers.OnlineTestController;
import com.example.online_testing.Controllers.UserController;
import com.example.online_testing.ErrorHandling.ApiError;
import com.example.online_testing.Models.*;
import com.example.online_testing.Repositories.*;
import com.example.online_testing.Services.OnlineTestService;
import org.apache.commons.io.IOUtils;
import org.hibernate.event.internal.OnLockVisitor;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.online_testing.BrowserControllerTests.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OnlineTestController.class)
public class OnlineTestControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OnlineTestService onlineTestService;


    @Test
    public void testGetOnlineTests() throws Exception {
        OnlineTest onlineTest = new OnlineTest("Test1", null, null, null, null);
        List<OnlineTest> onlineTests = Arrays.asList(onlineTest);
        given(onlineTestService.getAllOnlineTests()).willReturn(onlineTests);

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

        given(onlineTestService.getOnlineTestByID(Long.valueOf(1))).willReturn(new ResponseEntity(onlineTest, HttpStatus.OK));

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
        List<String> errors = new ArrayList<>();
        errors.add("Online test does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        given(onlineTestService.getOnlineTestByID(Long.valueOf(1))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .get("/onlineTest/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Online test does not exist!"));
    }

    @Test
    public void deleteOnlineTestDoesExist() throws Exception
    {
        JSONObject jo = new JSONObject();
        jo.put("message", "Online test is successfully deleted!");
        given(onlineTestService.deleteOnlineTestByID(Long.valueOf(1))).willReturn(new ResponseEntity(jo.toString(), HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders.delete("/onlineTest/{id}", 1) )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Online test is successfully deleted!"));
    }

    @Test
    public void deleteOnlineTestDoesNotExist() throws Exception
    {
        List<String> errors = new ArrayList<>();
        errors.add("Online test does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        given(onlineTestService.deleteOnlineTestByID(Long.valueOf(1))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders.delete("/onlineTest/{id}", 1) )
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Online test does not exist!"));
    }

    @Test
    public void getOnlineTestsServerDoesExist() throws Exception
    {
        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        server.setID(Long.valueOf(1));

        OnlineTest onlineTest = new OnlineTest("Test1", null, server, null, null);
        onlineTest.setID(Long.valueOf(1));
        List<OnlineTest> onlineTests = Arrays.asList(onlineTest);
        given(onlineTestService.getOnlineTestsServers(server.getID())).willReturn(onlineTests);

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
        List<OnlineTest> onlineTests = Arrays.asList();
        given(onlineTestService.getOnlineTestsServers(Long.valueOf(1))).willReturn(onlineTests);

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
        User user = new User(null, "zramic1", "zramic1@etf.unsa.ba");
        user.setID(Long.valueOf(1));

        OnlineTest onlineTest = new OnlineTest("Test1", null, null, user, null);
        onlineTest.setID(Long.valueOf(1));
        List<OnlineTest> onlineTests = Arrays.asList(onlineTest);
        given(onlineTestService.getOnlineTestsUsers(user.getID())).willReturn(onlineTests);

        mvc.perform(MockMvcRequestBuilders
                .get("/onlineTestsUser/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tests").value(onlineTest.getTests()));
    }

    @Test
    public void getOnlineTestsUsersDoesNotExist() throws Exception
    {

        List<OnlineTest> onlineTests = Arrays.asList();
        given(onlineTestService.getOnlineTestsUsers(Long.valueOf(1))).willReturn(onlineTests);

        mvc.perform(MockMvcRequestBuilders
                .get("/onlineTestsUser/{id}", 1)
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

        OnlineTest onlineTest = new OnlineTest("Test1", null, null, null, document);
        onlineTest.setID(Long.valueOf(1));
        given(onlineTestService.getOnlineTestGSPECDocument(document.getID())).willReturn(new ResponseEntity(onlineTest, HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders
                .get("/onlineTestsGSPECDocument/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tests").value(onlineTest.getTests()));
    }

    @Test
    public void getOnlineTestGSPECDocumentDoesNotExist() throws Exception
    {
        List<String> errors = new ArrayList<>();
        errors.add("Online test does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        given(onlineTestService.getOnlineTestGSPECDocument(Long.valueOf(1))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .get("/onlineTestsGSPECDocument/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Online test does not exist!"));
    }

    @Test
    public void createOnlineTestServerDoesExist() throws Exception
    {
        GSPECDocument document = new GSPECDocument("Document1", null);
        document.setID(Long.valueOf(1));

        User user = new User(null, "zramic1", "zramic1@etf.unsa.ba");
        user.setID(Long.valueOf(1));

        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        server.setID(Long.valueOf(1));

        OnlineTest onlineTest = new OnlineTest("Test1", null, 1, 1, 1);
        given(this.onlineTestService.saveOnlineTest(ArgumentMatchers.any(OnlineTest.class))).willReturn(new ResponseEntity(onlineTest, HttpStatus.CREATED));

        mvc.perform(MockMvcRequestBuilders
                .post("/addOnlineTest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(onlineTest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tests").value(onlineTest.getTests()));
    }

    @Test
    public void createOnlineTestServerDoesNotExist() throws Exception
    {
        OnlineTest onlineTest = new OnlineTest("Test1", null, null, null, null);
        List<String> errors = new ArrayList<>();
        errors.add("Server does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        given(this.onlineTestService.saveOnlineTest(ArgumentMatchers.any(OnlineTest.class))).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .post("/addOnlineTest")
                .content(asJsonString(onlineTest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Server does not exist!"));
    }

    @Test
    public void updateOnlineTestDoesExist() throws Exception
    {
        OnlineTest onlineTest = new OnlineTest("Test1", null, 1, 1, 1);
        onlineTest.setID(Long.valueOf(1));

        Server server = new Server("http://nekiserver1.com", 3306, "1", null);
        server.setID(Long.valueOf(1));

        GSPECDocument document = new GSPECDocument("Document1", null);
        document.setID(Long.valueOf(1));

        User user = new User(null, "zramic1", "zramic1@etf.unsa.ba");
        user.setID(Long.valueOf(1));

        OnlineTest onlineTest1 = new OnlineTest("Test2", null, 1, 1, 1);

        given(this.onlineTestService.updateOnlineTest(ArgumentMatchers.any(OnlineTest.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(onlineTest1, HttpStatus.OK));

        mvc.perform(MockMvcRequestBuilders
                .put("/updateOnlineTest/{id}", 1)
                .content(asJsonString(onlineTest1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tests").value("Test2"));
    }

    @Test
    public void updateOnlineTestDoesNotExist() throws Exception
    {
        List<String> errors = new ArrayList<>();
        errors.add("The online test you want to update does not exist!");
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Record Not Found", errors);
        OnlineTest onlineTest = new OnlineTest("Test1", null, null, null, null);
        given(this.onlineTestService.updateOnlineTest(ArgumentMatchers.any(OnlineTest.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .put("/updateOnlineTest/{id}", 2)
                .content(asJsonString(onlineTest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("The online test you want to update does not exist!"));
    }

    @Test
    public void handleMethodArgumentNotValid() throws Exception {

        OnlineTest onlineTest = new OnlineTest("T", null, null, null, null);

        mvc.perform(MockMvcRequestBuilders
                .put("/updateOnlineTest/{id}", 1)
                .content(asJsonString(onlineTest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Online test name must be between 5 and 30 characters!"));
    }

    @Test
    public void handleMethodArgumentTypeMismatch() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/onlineTest/{id}", "id")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("id should be of type java.lang.Long"));
    }

    @Test
    public void handleHttpRequestMethodNotSupported() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .patch("/onlineTest/{id}", "id")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("PATCH method is not supported for this request. Supported methods are GET DELETE "));

    }

    @Test
    public void handleAlreadyExistsException() throws Exception {
        OnlineTest onlineTest = new OnlineTest("Test1", null, null, null, null);
        onlineTest.setID(Long.valueOf(1));

        OnlineTest onlineTest1 = new OnlineTest("Test1", null, null, null, null);

        List<String> errors = new ArrayList<>();
        errors.add("Online test already exists!");
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Record Already Exists", errors);

        given(this.onlineTestService.updateOnlineTest(ArgumentMatchers.any(OnlineTest.class), ArgumentMatchers.anyLong())).willReturn(new ResponseEntity(apiError, apiError.getStatus()));

        mvc.perform(MockMvcRequestBuilders
                .put("/updateOnlineTest/{id}", 1)
                .content(asJsonString(onlineTest1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("Online test already exists!"));
    }

}