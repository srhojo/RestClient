package com.srhojo.utils.restclient;

/**
 *
 * @author: srhojo
 * @see <a href="https://github.com/srhojo">GitHub</a>
 *
 */
@FunctionalInterface
public interface RestClientExecutor {

    <T> T execute();
}
