package com.fcastro.catering;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CateringJobRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    CateringJobRepository cateringJobRepository;

    @Test
    public void findAllByStatus(){
        //given
        CateringJob cateringJob1 =
                CateringJob.builder()
                        .customerName("Maria")
                        .phoneNumber("123456789")
                        .email("maria@maria.com")
                        .noOfGuests(50)
                        .menu("standard")
                        .status(Status.IN_PROGRESS).build();

        CateringJob cateringJob2 = CateringJob.builder()
                .customerName("John")
                .phoneNumber("987654321")
                .email("john@john.com")
                .noOfGuests(100)
                .menu("special")
                .status(Status.COMPLETED).build();

        entityManager.persistAndFlush(cateringJob1);
        entityManager.persistAndFlush(cateringJob2);


        //when
        List<CateringJob> cateringJobs = cateringJobRepository.findByStatus(Status.COMPLETED);

        //then
        assertThat(cateringJobs).isNotNull();
        assertThat(cateringJobs).hasSize(1);
        assertThat(cateringJobs.get(0).getStatus()).isEqualByComparingTo(Status.COMPLETED);
    }
}
