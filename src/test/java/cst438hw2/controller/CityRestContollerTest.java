package cst438hw2.controller;


import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cst438hw2.domain.CityInfo;
import cst438hw2.service.CityService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(CityRestController.class)
public class CityRestContollerTest {


    @MockBean
    private CityService cityService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<CityInfo> jsonCityAttempt;

    @BeforeEach
    public void setUpEach() {
        MockitoAnnotations.initMocks(this);
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void test1() throws Exception {

       
        CityInfo cityInfo = new CityInfo(1, "TestCity", "TST", "Test Country", "Test District",
                100000, 80.0, "12:00 AM");
        given(cityService.getCityInfo("TestCity"))
                .willReturn(new ResponseEntity<CityInfo>(cityInfo, HttpStatus.OK));

        // test by making using URL
        // www.localhost.com:8080/api/city/TestCity
        MockHttpServletResponse response = mvc.perform(get("/api/cities/TestCity")).andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        System.out.println("Mock Response:" + response.getContentAsString());

        CityInfo cityResult = jsonCityAttempt.parseObject(response.getContentAsString());

        CityInfo expectedResult = new CityInfo(1, "TestCity", "TST", "Test Country",
                "Test District", 100000, 80.0, "12:00 AM");

    
        assertThat(cityResult).isEqualTo(expectedResult);

    }

    @Test
    public void test2() throws Exception {
        given(cityService.getCityInfo("UnknownCity"))
                .willReturn(new ResponseEntity<CityInfo>(HttpStatus.NOT_FOUND));

        // test by making using URL
        // www.localhost.com:8080/api/city/UnknownCIty 
        MockHttpServletResponse response = mvc.perform(get("/api/cities/UnknownCity")).andReturn()
                .getResponse();

       
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }

}
