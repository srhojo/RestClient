package com.srhojo.utils.restclient;

/**
 * Author: srhojo
 * URL: https://github.com/srhojo
 */
@FunctionalInterface
public interface RestClientExecutor {

    <T> T execute();
}
