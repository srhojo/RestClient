package io.github.srhojo.utils.restclient;

import io.github.srhojo.utils.restclient.client.RestClient;

/**
 * Base interface to build a builder that execute HTTP requests through
 * {@link RestClient}
 *
 *
 * @author: srhojo
 * @see <a href="https://github.com/srhojo">GitHub</a>
 *
 */
@FunctionalInterface
public interface RestClientExecutor {

    /**
     * Method to do the final action to execute the HTTP request.
     *
     * @param <T> Type of return parameter.
     * @return return value.
     */
    <T> T execute();

}
