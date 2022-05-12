package com.fcastro.catering.config;

import com.fcastro.catering.CateringJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

    private final CateringJobRepository cateringJobRepository;

    public ScheduledTasks(CateringJobRepository cateringJobRepository){
        this.cateringJobRepository = cateringJobRepository;
    }

    @Scheduled(fixedRate = 10000)
    public void reportOrderStats(){
        LOGGER.info("The total jobs now is {}", cateringJobRepository.count());
    }
}
