package io.github.srhojo.utils.restclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

/**
 *
 * Mandatory configuration to use this library. Use {@link Import} to load the
 * configuration
 *
 * @author: srhojo
 * @see <a href="https://github.com/srhojo">GitHub</a>
 *
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(value = "io.github.srhojo.utils.restclient.*")
public class RestClientConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
