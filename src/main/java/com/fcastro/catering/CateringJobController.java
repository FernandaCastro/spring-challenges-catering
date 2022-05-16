package com.fcastro.catering;

import com.fasterxml.jackson.databind.JsonNode;
import com.fcastro.catering.config.ImagesWebClient;
import com.fcastro.catering.config.Loggable;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("cateringJobs")
@AllArgsConstructor
public class CateringJobController {

    private final CateringJobRepository cateringJobRepository;
    private final ImagesWebClient imagesWebClient;

    @GetMapping
    public List<CateringJob> findAll() {
        return cateringJobRepository.findAll();
    }

    @GetMapping("/{id}")
    @Loggable
    public CateringJob findById(@PathVariable Long id) {
        return cateringJobRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/findByStatus")
    public List<CateringJob> findAllByStatus(@RequestParam Status status) {
        return cateringJobRepository.findByStatus(status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CateringJob create(@RequestBody CateringJob cateringJob) {
        return cateringJobRepository.save(cateringJob);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CateringJob update(@RequestBody CateringJob cateringJob, @PathVariable Long id) {
        if (cateringJobRepository.existsById(id)) {
            cateringJob.setId(id);
            return cateringJobRepository.save(cateringJob);
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CateringJob patchMenu(@RequestBody JsonNode json, @PathVariable Long id) {
        JsonNode menuJsonNode = json.get("menu");
        if (menuJsonNode == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }

        return cateringJobRepository.findById(id)
                .map(job ->{
                    job.setMenu(menuJsonNode.asText());
                    return cateringJobRepository.save(job);
                })
                .orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/surpriseMe")
    public Mono<String> getSurpriseImage() {
        return imagesWebClient.getRandomImage();
    }
}
