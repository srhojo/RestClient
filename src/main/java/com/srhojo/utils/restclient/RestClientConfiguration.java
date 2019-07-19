package com.srhojo.utils.restclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

/**
 * Author: srhojo URL: https://github.com/srhojo
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(value = "com.srhojo.utils.restclient.*")
public class RestClientConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
