package com.srhojo.utils.restclient.impl;

import com.srhojo.utils.restclient.RestClientExecutor;
import com.srhojo.utils.restclient.client.RestClient;
import com.srhojo.utils.restclient.entities.NameValuePair;
import com.srhojo.utils.restclient.entities.RestRequest;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Author: srhojo
 * URL: https://github.com/srhojo
 */
public class RestClientBuilderExecutor implements RestClientExecutor {


    private final RestClient restClient;
    private final RestRequest restRequest;

    public RestClientBuilderExecutor(RestClient restClient, RestRequest restRequest) {
        this.restClient = restClient;
        this.restRequest = restRequest;
    }

    private RestClientBuilderExecutor(final RestClient restClient) {
        this.restClient = restClient;
        this.restRequest = new RestRequest();
    }

    public static RestClientBuilderExecutor of(@NotNull final RestClient restClient) {
        return new RestClientBuilderExecutor(restClient);
    }

    public RestClientBuilderExecutor url(@NotNull final String url) {
        restRequest.setUrl(url);
        return this;
    }


    public RestClientBuilderExecutor method(@NotNull final HttpMethod method) {
        restRequest.setHttpMethod(method);
        return this;
    }

    public RestClientBuilderExecutor withRequest(@NotNull final Object request) {
        restRequest.setRequest(Optional.of(request));
        return this;
    }

    public RestClientBuilderExecutor withResponseType(@NotNull Class<?> responseType) {
        restRequest.setResponseType(Optional.of(responseType));
        return this;
    }

    public RestClientBuilderExecutor withHeaders(List<NameValuePair> headers) {
        restRequest.getHeaders().addAll(headers);
        return this;
    }

    public RestClientBuilderExecutor withQueryParams(List<NameValuePair> queryParams) {
        restRequest.getQueryParams().addAll(queryParams);
        return this;
    }

    @Override
    public <T> T execute() {
        return restClient.execute(restRequest);
    }
}
