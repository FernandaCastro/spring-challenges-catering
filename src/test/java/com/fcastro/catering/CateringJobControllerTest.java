package com.fcastro.catering;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.internal.filter.ValueNodes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CateringJobController.class)
public class CateringJobControllerTest {
    private static List<CateringJob> cateringJobList;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebTestClient webClient;

    @MockBean
    CateringJobRepository cateringJobRepository;

    @MockBean
    ImagesWebClient imagesWebClient;

    @BeforeAll
    private static void fetchCateringJobList() {
        cateringJobList = new ArrayList<>();
        cateringJobList.add(CateringJob.builder()
                .id(1L)
                .customerName("Maria")
                .phoneNumber("123456789")
                .email("maria@maria.com")
                .noOfGuests(50)
                .menu("standard")
                .status(Status.IN_PROGRESS).build());

        cateringJobList.add(CateringJob.builder()
                .id(2L)
                .customerName("John")
                .phoneNumber("987654321")
                .email("john@john.com")
                .noOfGuests(100)
                .menu("special")
                .status(Status.COMPLETED).build());
    }

    @Test
    public void whenFindAll_ReturnList() throws Exception {
        //given
        given(cateringJobRepository.findAll()).willReturn(cateringJobList);

        //when //then
        mockMvc.perform(get("/cateringJobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void whenFindById_ReturnCateringJob() throws Exception {
        //given
        given(cateringJobRepository.findById(anyLong())).willReturn(Optional.of(cateringJobList.get(0)));

        //when //then
        mockMvc.perform(get("/cateringJobs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void whenFindByStatus_ReturnCateringJob() throws Exception {
        //given
        given(cateringJobRepository.findByStatus(any(Status.class))).willReturn(List.of(cateringJobList.get(1)));

        //when //then
        mockMvc.perform(get("/cateringJobs/findByStatus?status=COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void whenPOSTNewCateringJob_ReturnSuccess() throws Exception{
        //given
        CateringJob cateringJob = CateringJob.builder()
                .id(10L)
                .customerName("Mary")
                .email("mary@mary.com")
                .menu("Standard")
                .noOfGuests(50)
                .status(Status.NOT_STARTED)
                .build();
        given(cateringJobRepository.save(any(CateringJob.class))).willReturn(cateringJob);

        //when //then
        mockMvc.perform(post("/cateringJobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json().build().writeValueAsString(cateringJob)))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenPUTExistingCateringJob_UpdateAndReturnSuccess() throws Exception{
        //given
        CateringJob cateringJob = CateringJob.builder()
                .id(10L)
                .customerName("Mary")
                .email("mary@mary.com")
                .menu("Standard")
                .noOfGuests(50)
                .status(Status.NOT_STARTED)
                .build();
        given(cateringJobRepository.existsById(anyLong())).willReturn(true);
        given(cateringJobRepository.save(any(CateringJob.class))).willReturn(cateringJob);

        //when //then
        mockMvc.perform(put("/cateringJobs/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json().build().writeValueAsString(cateringJob)))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenPUTNotExistCateringJob_ReturnNOT_FOUND(){
        //given
        CateringJob cateringJob = CateringJob.builder()
                .id(10L)
                .customerName("Mary")
                .email("mary@mary.com")
                .menu("Standard")
                .noOfGuests(50)
                .status(Status.NOT_STARTED)
                .build();
        given(cateringJobRepository.existsById(anyLong())).willReturn(false);

        //when //then
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            mockMvc.perform(put("/cateringJobs/10")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json().build().writeValueAsString(cateringJob)));
        });
        assertThat(thrown.getCause()).isInstanceOf(HttpClientErrorException.class);
        assertThat(((HttpClientErrorException)thrown.getCause()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenPATCHExistingCateringJob_UpdateAndReturnSuccess() throws Exception{
        //given
        CateringJob cateringJob = CateringJob.builder()
                .id(10L)
                .menu("Standard")
                .build();
        given(cateringJobRepository.findById(anyLong())).willReturn(Optional.ofNullable(cateringJob));
        given(cateringJobRepository.save(any(CateringJob.class))).willReturn(cateringJob);

        String menuJsonNode = "{\"menu\":\"Special\"}";

        //when //then
        mockMvc.perform(patch("/cateringJobs/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(menuJsonNode))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenPATCHNotExistCateringJob_ReturnNOT_FOUND() {
        //given
        CateringJob cateringJob = CateringJob.builder()
                .id(10L)
                .menu("Standard")
                .build();
        given(cateringJobRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        String menuJsonNode = "{\"menu\":\"Special\"}";

        //when //then
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            mockMvc.perform(patch("/cateringJobs/10")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(menuJsonNode));
        });
        assertThat(thrown.getCause()).isInstanceOf(HttpClientErrorException.class);
        assertThat(((HttpClientErrorException)thrown.getCause()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenPATCHNullMenu_ReturnBAD_REQUEST(){
        //given
        CateringJob cateringJob = CateringJob.builder()
                .id(10L)
                .menu("Standard")
                .build();
        String menuJsonNode = "{}";

        //when //then
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            mockMvc.perform(patch("/cateringJobs/10")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(menuJsonNode));
        });
        assertThat(thrown.getCause()).isInstanceOf(HttpClientErrorException.class);
        assertThat(((HttpClientErrorException)thrown.getCause()).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenNonExisting_whenFindById_ReturnHandledException() throws Exception {
        //given
        given(cateringJobRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        //when //then
        mockMvc.perform(get("/cateringJobs/100"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.errorType", is("HttpClientErrorException")))
                .andExpect(jsonPath("$.errorMessage", is("Resource was not found.")))
                .andExpect(jsonPath("$.path", is("/cateringJobs/100")));
    }

    @Test
    public void whenSurpriseMe_ReturnImageURL() throws Exception{
        //given
        String imageUrl = "https://foodish-api.herokuapp.com/images/dessert/dessert32.jpg";

        Mono<Image> imageMono = Mono.just(Image.builder().image(imageUrl).build());
        given(imagesWebClient.getRandomImage()).willReturn(imageMono);

        //when //then
        webClient.get().uri("/cateringJobs/surpriseMe")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Image.class)
                .value(image -> image.getImage(), equalTo(imageUrl));
    }
}
