package com.srhojo.utils.restclient.client;

import com.srhojo.utils.restclient.entities.RestRequest;

/**
 * Author: srhojo
 * URL: https://github.com/srhojo
 */
public interface RestClient {


    /**
     *
     * @param request RestRequest info
     * @param <T> Return parameter. Must be equals than request.responseType
     * @return Result of rest exchange.
     */
    <T> T execute(RestRequest request);
}
