package com.fcastro.catering;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CateringJobRepository extends CrudRepository<CateringJob, Long> {

    List<CateringJob> findAll();

    List<CateringJob> findByStatus(Status status);
}
