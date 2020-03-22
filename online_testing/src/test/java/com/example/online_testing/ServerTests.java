package com.example.online_testing;

import com.example.online_testing.Models.Role;
import com.example.online_testing.Models.RoleNames;
import com.example.online_testing.Models.Server;
import com.example.online_testing.Models.User;
import com.example.online_testing.Repositories.*;
import net.bytebuddy.asm.Advice;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class ServerTests {

    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private BrowserRepository browserRepository;
    @Autowired
    private OnlineTestRepository onlineTestRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;


    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    //GET

    @Test
    public void getAllServersTest() throws IOException {
        TestRestTemplate restTemplate = new TestRestTemplate();

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/servers", HttpMethod.GET, entity, String.class);

        String expected = "[{\"url\":\"http://nekiserver1.com\",\"port\":3306,\"status\":\"1\",\"userID\":{\"roleID\":{\"id\":1,\"role_name\":\"ADMIN\"},\"username\":\"zramic1\",\"password\":\"i12*67H8\",\"email\":\"zramic1@etf.unsa.ba\",\"id\":1},\"id\":1},{\"url\":\"http://nekiserver2.com\",\"port\":3310,\"status\":\"1\",\"userID\":{\"roleID\":{\"id\":1,\"role_name\":\"ADMIN\"},\"username\":\"zramic1\",\"password\":\"i12*67H8\",\"email\":\"zramic1@etf.unsa.ba\",\"id\":1},\"id\":2}]";

        assertEquals(expected, response.getBody());
    }

    @Test
    public void getServerByIDNotExist() throws IOException {
        TestRestTemplate restTemplate = new TestRestTemplate();

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/server/0", HttpMethod.GET, entity, String.class);

        assertEquals("Server does not exist!", response.getBody());
    }

    @Test
    public void getServerByIDExist() throws IOException {
        TestRestTemplate restTemplate = new TestRestTemplate();

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/server/1", HttpMethod.GET, entity, String.class);

        String expected = "{\"url\":\"http://nekiserver1.com\",\"port\":3306,\"status\":\"1\",\"userID\":{\"roleID\":{\"id\":1,\"role_name\":\"ADMIN\"},\"username\":\"zramic1\",\"password\":\"i12*67H8\",\"email\":\"zramic1@etf.unsa.ba\",\"id\":1},\"id\":1}";

        System.out.print("sssssssssssssssss");
        System.out.print(response.getBody());
        System.out.print("sssssssssssssssss");
        System.out.print(expected);
        System.out.print("sssssssssssssssss");
        assertEquals(expected, response.getBody());
    }

    @Test
    public void getUsersServersNotExist() {
        TestRestTemplate restTemplate = new TestRestTemplate();

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/userServers/0", HttpMethod.GET, entity, String.class);

        System.out.print("sssssssssssssssss");
        System.out.print(response.getBody());
        System.out.print("sssssssssssssssss");
        assertEquals("[]", response.getBody());
    }

    @Test
    public void getUsersServersExist() {
        TestRestTemplate restTemplate = new TestRestTemplate();

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/userServers/1", HttpMethod.GET, entity, String.class);

        String expected = "[{\"url\":\"http://nekiserver1.com\",\"port\":3306,\"status\":\"1\",\"userID\":{\"roleID\":{\"id\":1,\"role_name\":\"ADMIN\"},\"username\":\"zramic1\",\"password\":\"i12*67H8\",\"email\":\"zramic1@etf.unsa.ba\",\"id\":1},\"id\":1},{\"url\":\"http://nekiserver2.com\",\"port\":3310,\"status\":\"1\",\"userID\":{\"roleID\":{\"id\":1,\"role_name\":\"ADMIN\"},\"username\":\"zramic1\",\"password\":\"i12*67H8\",\"email\":\"zramic1@etf.unsa.ba\",\"id\":1},\"id\":2}]";

        System.out.print("sssssssssssssssss");
        System.out.print(response.getBody());
        System.out.print("sssssssssssssssss");
        assertEquals(expected, response.getBody());
    }
}
