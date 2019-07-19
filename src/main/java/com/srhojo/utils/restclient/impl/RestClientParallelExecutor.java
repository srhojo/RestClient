package com.srhojo.utils.restclient.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.srhojo.utils.restclient.RestClientExecutor;
import com.srhojo.utils.restclient.client.RestClient;
import com.srhojo.utils.restclient.entities.RestRequest;

/**
 * Author: srhojo URL: https://github.com/srhojo
 */
public class RestClientParallelExecutor implements RestClientExecutor {

    private final RestClient restClient;
    private final List<RestRequest> restRequests;

    private RestClientParallelExecutor(final RestClient restClient) {
        this.restClient = restClient;
        this.restRequests = new ArrayList<>();
    }

    public static RestClientParallelExecutor of(final RestClient restClient) {
        return new RestClientParallelExecutor(restClient);
    }

    public RestClientParallelExecutor addRequest(final RestRequest restRequest) {
        this.restRequests.add(restRequest);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T execute() {
        final Stream<RestClientBuilderExecutor> restClientBuilderExecutorStream = restRequests.stream()
                .map(this::createBuilderExecutor).collect(Collectors.toList()).stream();
        return (T) restClientBuilderExecutorStream.parallel().map(RestClientBuilderExecutor::execute)
                .collect(Collectors.toList());
    }

    /**
     * Method to constructor a builder Executor
     *
     * @param restRequest request
     * @return new executor
     */
    private RestClientBuilderExecutor createBuilderExecutor(final RestRequest restRequest) {
        final RestClientBuilderExecutor executor = RestClientBuilderExecutor.of(restClient).url(restRequest.getUrl())
                .method(restRequest.getHttpMethod()).withQueryParams(restRequest.getQueryParams())
                .withHeaders(restRequest.getHeaders());

        final Optional<?> request = restRequest.getRequest();
        if (request.isPresent()) {
            executor.withRequest(request.get());
        }
        if (restRequest.getResponseType().isPresent()) {
            executor.withResponseType(restRequest.getResponseType().get());
        }

        return executor;
    }

}
