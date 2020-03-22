package com.example.online_testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class BrowserTests {

    @Test
    public void getAllBrowsersTest() throws IOException {
        TestRestTemplate restTemplate = new TestRestTemplate();

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/browsers", HttpMethod.GET, entity, String.class);

        String expected =  "[{\"name\":\"Mozila Firefox\",\"version\":\"Mobile\",\"serverID\":{\"url\":\"http://nekiserver1.com\",\"port\":3306,\"status\":\"1\",\"userID\":{\"roleID\":{\"id\":1,\"role_name\":\"ADMIN\"},\"username\":\"zramic1\",\"password\":\"i12*67H8\",\"email\":\"zramic1@etf.unsa.ba\",\"id\":1},\"id\":1},\"idServer\":0,\"id\":1},{\"name\":\"Google Chrome\",\"version\":\"Desktop\",\"serverID\":{\"url\":\"http://nekiserver2.com\",\"port\":3310,\"status\":\"1\",\"userID\":{\"roleID\":{\"id\":1,\"role_name\":\"ADMIN\"},\"username\":\"zramic1\",\"password\":\"i12*67H8\",\"email\":\"zramic1@etf.unsa.ba\",\"id\":1},\"id\":2},\"idServer\":0,\"id\":2}]" ;

        assertEquals(expected, response.getBody());
    }

    @Test
    public void getBrowserByIDNotExist() throws IOException {
        TestRestTemplate restTemplate = new TestRestTemplate();

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/browser/0", HttpMethod.GET, entity, String.class);

        assertEquals("Browser does not exist!", response.getBody());
    }

    @Test
    public void getBrowserByIDExist() throws IOException {
        TestRestTemplate restTemplate = new TestRestTemplate();

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/browser/1", HttpMethod.GET, entity, String.class);

        String expected =  "{\"name\":\"Mozila Firefox\",\"version\":\"Mobile\",\"serverID\":{\"url\":\"http://nekiserver1.com\",\"port\":3306,\"status\":\"1\",\"userID\":{\"roleID\":{\"id\":1,\"role_name\":\"ADMIN\"},\"username\":\"zramic1\",\"password\":\"i12*67H8\",\"email\":\"zramic1@etf.unsa.ba\",\"id\":1},\"id\":1},\"idServer\":0,\"id\":1}" ;

        assertEquals(expected, response.getBody());
    }

    @Test
    public void getBrowsersServerNotExist() {
        TestRestTemplate restTemplate = new TestRestTemplate();

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/browsersServer/0", HttpMethod.GET, entity, String.class);

        assertEquals("[]", response.getBody());
    }

    @Test
    public void getBrowsersServerExist() {
        TestRestTemplate restTemplate = new TestRestTemplate();

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/browsersServer/1", HttpMethod.GET, entity, String.class);

        String expected =  "[{\"name\":\"Mozila Firefox\",\"version\":\"Mobile\",\"serverID\":{\"url\":\"http://nekiserver1.com\",\"port\":3306,\"status\":\"1\",\"userID\":{\"roleID\":{\"id\":1,\"role_name\":\"ADMIN\"},\"username\":\"zramic1\",\"password\":\"i12*67H8\",\"email\":\"zramic1@etf.unsa.ba\",\"id\":1},\"id\":1},\"idServer\":0,\"id\":1}]" ;

        assertEquals(expected, response.getBody());
    }
}
