package com.fcastro.catering;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CateringJobController.class)
public class CateringJobControllerTest {
    private static List<CateringJob> cateringJobList;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CateringJobRepository cateringJobRepository;

    @BeforeAll
    private static void fetchCateringJobList(){
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
    public void whenFindAll_ReturnList() throws Exception{
        //given
        given(cateringJobRepository.findAll()).willReturn(cateringJobList);

        //when //then
        mockMvc.perform(get("/cateringJobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void whenFindById_ReturnCateringJob() throws Exception{
        //given
        given(cateringJobRepository.findById(anyLong())).willReturn(Optional.of(cateringJobList.get(0)));

        //when //then
        mockMvc.perform(get("/cateringJobs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void whenFindByStatus_ReturnCateringJob() throws Exception{
        //given
        given(cateringJobRepository.findByStatus(any(Status.class))).willReturn(List.of(cateringJobList.get(1)));

        //when //then
        mockMvc.perform(get("/cateringJobs/findByStatus?status=COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
