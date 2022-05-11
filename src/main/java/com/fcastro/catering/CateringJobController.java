package com.fcastro.catering;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("cateringJobs")
@AllArgsConstructor
public class CateringJobController {

    private final CateringJobRepository cateringJobRepository;

    @GetMapping
    public List<CateringJob> findAll(){
        return cateringJobRepository.findAll();
    }

    @GetMapping("/{id}")
    public CateringJob findById(@PathVariable Long id){
        return cateringJobRepository.findById(id)
                .orElseThrow(()->new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/findByStatus")
    public List<CateringJob> findAllByStatus(@RequestParam Status status){
        return cateringJobRepository.findByStatus(status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CateringJob create(@RequestBody CateringJob cateringJob){
        return cateringJobRepository.save(cateringJob);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CateringJob update(@RequestBody CateringJob cateringJob, @PathVariable Long id){
        if (cateringJobRepository.existsById(id)) {
            cateringJob.setId(id);
            return cateringJobRepository.save(cateringJob);
        } else{
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }
}
