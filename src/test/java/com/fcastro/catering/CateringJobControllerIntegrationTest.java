package com.fcastro.catering;

import com.fcastro.catering.client.Image;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.startsWith;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CateringJobControllerIntegrationTest {

    @Autowired
    WebTestClient webClient;

    @Test
    public void whenSurpriseMe_ReturnImageURL() throws Exception{
        String imageUrl = "https://foodish-api.herokuapp.com/images";

        //when //then
        webClient.get().uri("/cateringJobs/surpriseMe")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Image.class)
                .value(Image::getImage, startsWith(imageUrl));
    }
}
