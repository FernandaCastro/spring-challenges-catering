package com.fcastro.catering;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Component
@Endpoint(id="custom-stats")
public class CateringJobEndpoint {

    private final CateringJobRepository cateringJobRepository;

    public CateringJobEndpoint(CateringJobRepository cateringJobRepository){
        this.cateringJobRepository = cateringJobRepository;
    }

    @ReadOperation
    public Map<Status, Long> reportJobsPerStatus(){
        return cateringJobRepository.findAll()
                .stream()
                .collect(groupingBy(CateringJob::getStatus, Collectors.counting()));

    }
}
