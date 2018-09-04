package com.springboot.projects.controllers;

import com.springboot.projects.models.Question;
import com.springboot.projects.services.SurveyService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@WebMvcTest(SurveyController.class)
public class SurveyControllerTest {

    @MockBean
    SurveyService surveyService;

    @Autowired
    MockMvc mockMvc;
    @Test
    public void retrieveQuestionsForASurvey() throws Exception {
        Question mockQuestion = new Question("Question1",
                "Largest Country in the World", "Russia", Arrays.asList(
                "India", "Russia", "United States", "China"));
        Mockito.when(surveyService.retrieveQuestion(Mockito.anyString(), Mockito.anyString())).thenReturn(mockQuestion);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/surveys/Survey1/questions/Question1").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"id\":\"Question1\",\"description\":\"Largest Country in the World\",\"correctAnswer\":\"Russia\"}";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }

    @Test
    public void createSurveyQuestion() throws Exception {
        Question mockQuestion = new Question("1", "Smallest Number", "1",
                Arrays.asList("1", "2", "3", "4"));
        String questionJson = "{\"description\":\"Smallest Number\",\"correctAnswer\":\"1\",\"options\":[\"1\",\"2\",\"3\",\"4\"]}";
        //surveyService.addQuestion to respond back with mockQuestion
        Mockito.when(surveyService.addQuestion(Mockito.anyString(), Mockito.any(Question.class))).thenReturn(mockQuestion);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/surveys/Survey1/questions")
                .accept(MediaType.APPLICATION_JSON).content(questionJson).contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(requestBuilder).andReturn();

        Assert.assertEquals(201, response.getResponse().getStatus());
        Assert.assertEquals("http://localhost/surveys/Survey1/questions/1",
                response.getResponse().getHeader(HttpHeaders.LOCATION));
    }
}
