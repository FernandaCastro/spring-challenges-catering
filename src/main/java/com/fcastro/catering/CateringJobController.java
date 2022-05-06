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
    @ResponseBody
    public List<CateringJob> all(){
        return cateringJobRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public CateringJob oneById(@PathVariable Long id){
        return cateringJobRepository.findById(id)
                .orElseThrow(()->new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }


}
