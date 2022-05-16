package com.fcastro.catering.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("@annotation(Loggable)")
    public void executeLogging(){
    }

    @Around("executeLogging()")
    public Object logMethodAroundCall(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String className = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        String args = Arrays.toString(proceedingJoinPoint.getArgs());

        LOGGER.info("Request: {} - {} - Args: {}",
                className,
                methodName,
                args);

        Object result = proceedingJoinPoint.proceed();

        LOGGER.info("Response: {} - {} - Args: {} - Return: {}",
                className,
                methodName,
                args,
                result != null ? result : "null");

        return result;
    }
}
