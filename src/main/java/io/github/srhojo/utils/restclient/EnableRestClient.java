package io.github.srhojo.utils.restclient;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * @author hojo
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
@Import(value = { RestClientConfiguration.class })
public @interface EnableRestClient {

}
