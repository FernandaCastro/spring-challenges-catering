package com.fcastro.catering.config;

import com.fcastro.catering.CateringJob;
import com.fcastro.catering.CateringJobRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public CateringJobRepository cateringJobRepository;

    @Bean
    public FlatFileItemReader<CateringJob> reader() {

        return new FlatFileItemReaderBuilder<CateringJob>()
                .name("cateringJobReader")
                .resource(new ClassPathResource("upload.csv"))
                .linesToSkip(1)
                .delimited()
                .names("id","customerName","phoneNumber","email","menu","noOfGuests","status")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<CateringJob>() {{
                    setTargetType(CateringJob.class);
                }}).build();
    }

    @Bean
    public RepositoryItemWriter<CateringJob> writer() {
        RepositoryItemWriter<CateringJob> writer = new RepositoryItemWriter<>();
        writer.setRepository(cateringJobRepository);
        return writer;
    }

    @Bean
    protected Step step() {
        return stepBuilderFactory.get("step")
                .<CateringJob, CateringJob> chunk(10)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public Job importCateringJob(){
        return jobBuilderFactory.get("importCateringJob")
                .start(step())
                .build();
    }


}
