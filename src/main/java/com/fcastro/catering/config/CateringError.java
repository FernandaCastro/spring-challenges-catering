package com.fcastro.catering.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class CateringError {
    private Long timestamp;
    private int status;
    private String errorType;
    private String errorMessage;
    private String path;
}
