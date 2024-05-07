package com.karty.kartyjavatest.common;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class CommonLogger {
    private static final Logger logger = LoggerFactory.getLogger(CommonLogger.class);
    private final HttpServletRequest request;

    @Autowired
    public CommonLogger(HttpServletRequest request) {
        this.request = request;
    }

    @Before("execution(* com.karty.kartyjavatest.controller.*.*(..) )")
    public void logRequest(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().toString();
        String methodName = joinPoint.getSignature().getName();

        String requestUrl = request.getRequestURL().toString();
        String httpMethod = request.getMethod();
        String args = Arrays.stream(joinPoint.getArgs()).map(Object::toString).collect(Collectors.joining(", "));

        logger.info("{} : {}() Invoked", className, methodName);
        logger.info("Request: {} {} Request Body/Params: [{}]", httpMethod, requestUrl, args);
    }

    @AfterReturning(pointcut = "execution(* com.karty.kartyjavatest.controller.*.*(..) )", returning = "result")
    public void logResponse(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getTarget().getClass().toString();
        String methodName = joinPoint.getSignature().getName();

        String requestUrl = request.getRequestURL().toString();
        String httpMethod = request.getMethod();
        String response = (result != null) ? result.toString() : "null";

        logger.info("Response: {} {} Response Body: [{}]", httpMethod, requestUrl, response);
        logger.info("{} : {}() : Ended ", className, methodName);
    }
}