package com.fcastro.catering;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CateringJobEndpointTest {

    @Mock
    private CateringJobRepository cateringJobRepository;

    @InjectMocks
    private CateringJobEndpoint cateringJobEndpoint;

    @Test
    public void givenCateringJobList_whenReportJobPerStatus_returnMap(){
        //given
        List<CateringJob> jobs = List.of(
                CateringJob.builder().status(Status.NOT_STARTED).build(),
                CateringJob.builder().status(Status.IN_PROGRESS).build(),
                CateringJob.builder().status(Status.COMPLETED).build(),
                CateringJob.builder().status(Status.CANCELED).build()
        );
        given(cateringJobRepository.findAll()).willReturn(jobs);

        //when
        Map stats = cateringJobEndpoint.reportJobsPerStatus();

        //then
        assertThat(stats.size()).isEqualTo(4);
        assertThat(stats.get(Status.NOT_STARTED)).isEqualTo(1L);
        assertThat(stats.get(Status.IN_PROGRESS)).isEqualTo(1L);
        assertThat(stats.get(Status.COMPLETED)).isEqualTo(1L);
        assertThat(stats.get(Status.CANCELED)).isEqualTo(1L);
    }
}
