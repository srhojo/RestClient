package com.srhojo.utils.restclient.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.srhojo.utils.restclient.entities.NameValuePair;
import com.srhojo.utils.restclient.entities.RestRequest;
import com.srhojo.utils.restclient.exceptions.RestClientException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: srhojo
 * URL: https://github.com/srhojo
 */
@Aspect
@Component
public class RestClientLoggerAop {

    private static final String HANDLED_EXCEPTION = "RestClient AOP: Handled exception: [ HttpCode: {}, Code: {}, Detail: {} ].";
    private static final String BEFORE_AOP = "RestClient AOP Before: Executing:[ {} ].";
    private static final String ELAPSED_TIME = "RestClient AOP: Method executed:[ {} ], elapsed time: {} ms";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("EmptyMethod")
    @Pointcut("within(@com.srhojo.utils.restclient.aop.RestClientLogger *)")
    public void markedAsLogged() {
        // Do nothing.
    }


    @Before("markedAsLogged()")
    public void logBefore(final JoinPoint joinPoint) {
        logger.info(BEFORE_AOP, joinPoint.getSignature());
    }

    @Around("markedAsLogged()")
    public Object executionAround(final ProceedingJoinPoint joinPoint) throws Throwable {
        final long startMillis = System.currentTimeMillis();
        Object proceed;
        try {
            printLogRestRequest((RestRequest) joinPoint.getArgs()[0]);
            proceed = joinPoint.proceed();
        } catch (final RestClientException rce) {
            logger.error(HANDLED_EXCEPTION, rce.getStatus(), rce.getCode(), rce.getDetails());
            throw rce;
        } finally {
            logger.info(ELAPSED_TIME, joinPoint.getSignature(), System.currentTimeMillis() - startMillis);
        }
        return proceed;
    }


    private void printLogRestRequest(final RestRequest restRequest) {
        logger.info("@@@ RC - REQUEST OBJECT @@@");
        logger.info("@@@ RC - Method: [ {} ]", restRequest.getHttpMethod());
        logger.info("@@@ RC - URI: [ {} ]", restRequest.getUrl());
        logger.info("@@@ RC - QueryParams: [ {}]", listToString(restRequest.getQueryParams()));
        logger.info("@@@ RC - Custom Headers: [ {}]", listToString(restRequest.getHeaders()));
        logger.info("@@@ RC - Request: [ {} ]", restRequest.getRequest().isPresent() ? objectToString(restRequest.getRequest().get()) : "");
        logger.info("@@@ RC - Response: [ {} ]", restRequest.getResponseType().isPresent() ? restRequest.getResponseType().get() : "Void");
        logger.info("@@@ RC - @@@@@@@@@@");
    }

    private String listToString(List<NameValuePair> queryParams) {
        StringBuilder sb = new StringBuilder();
        queryParams.forEach(nameValue -> sb.append(String.format(" %s : %s", nameValue.getName(), nameValue.getValue())).append(";"));
        return sb.toString();
    }

    private String objectToString(final Object o) {
        final ObjectMapper om = new ObjectMapper();
        try {
            return om.writeValueAsString(o);
        } catch (final JsonProcessingException e) {
            return "Exception parsing entity yo string, error: " + e.getMessage();
        }
    }

}
