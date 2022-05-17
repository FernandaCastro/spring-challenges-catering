package com.fcastro.catering.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ImagesWebClient {

    private static final String IMAGE_API = "https://foodish-api.herokuapp.com";
    private final WebClient imagesWebClient;

    @Autowired
    public ImagesWebClient() {
        this.imagesWebClient = WebClient.builder().baseUrl(IMAGE_API).build();
    }

    public Mono<Image>  getRandomImage(){
       return imagesWebClient.get()
                .uri("/api")
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new HttpClientErrorException(HttpStatus.BAD_GATEWAY)))
		        .onStatus(HttpStatus::is5xxServerError, response -> Mono.just(new HttpClientErrorException(HttpStatus.BAD_GATEWAY)))
                .bodyToMono(Image.class);
    }
}
