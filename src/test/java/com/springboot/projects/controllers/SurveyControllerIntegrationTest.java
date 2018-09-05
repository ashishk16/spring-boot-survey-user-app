package com.springboot.projects.controllers;

import com.springboot.projects.Application;
import com.springboot.projects.models.Question;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyControllerIntegrationTest {

    @LocalServerPort
    private int port;


    TestRestTemplate template = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    @Before
    public void before() {
        headers.add("Authorization", getHeaderValue("user1", "secret1"));
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    private String getHeaderValue(String username, String password) {
        String auth = username + ":"+ password;
        byte[] encodedAuth = Base64.encode(auth.getBytes(Charset.forName("US-ASCII")));
        return "Basic "+new String(encodedAuth);
    }

    @Test
    public void testRetrieveSurveyQuestion() throws JSONException {

        String url = getUrl("/surveys/Survey1/questions/Question1");
//        String output = testRestTemplate.getForObject(url, String.class);  //gives output in xml format
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> output = template.exchange(url, HttpMethod.GET, entity, String.class);

        System.out.println("output: "+output);
        String expected = "{\"id\":\"Question1\",\"description\":\"Largest Country in the World\",\"correctAnswer\":\"Russia\"}";
        JSONAssert.assertEquals(expected, output.getBody(), false);
    }


    @Test
    public void shouldSaveSurveyQuestion() throws JSONException {

        Question question = new Question("DOESN'T MATTER", "Smallest Number",
                "1", Arrays.asList("1", "2", "3", "4"));
        String url = getUrl("/surveys/Survey1/questions");
        HttpEntity<?> entity = new HttpEntity<Question>(question, headers);

        ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, entity, String.class);

        String actualLocation = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
        assertTrue(actualLocation.contains("/surveys/Survey1/questions/"));
    }

    private String getUrl(String requestPath) {
        return "http://localhost:" + port
        + requestPath;
    }
}
