package com.fcastro.catering;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CateringJobEndpointIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CateringJobRepository cateringJobRepository;

    @Test
    public void givenCateringJobList_whenGETCustomStats_ReturnJsonWithMap() throws Exception{
        //given
        List<CateringJob> jobs = List.of(
                CateringJob.builder().status(Status.NOT_STARTED).build(),
                CateringJob.builder().status(Status.IN_PROGRESS).build(),
                CateringJob.builder().status(Status.COMPLETED).build(),
                CateringJob.builder().status(Status.CANCELED).build()
        );
        given(cateringJobRepository.findAll()).willReturn(jobs);

        //when //then
        mockMvc.perform(get("/actuator/custom-stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(4)))
                .andExpect(jsonPath("$['NOT_STARTED']").value(1))
                .andExpect(jsonPath("$['IN_PROGRESS']").value(1))
                .andExpect(jsonPath("$['COMPLETED']").value(1))
                .andExpect(jsonPath("$['CANCELED']").value(1));
    }

}
