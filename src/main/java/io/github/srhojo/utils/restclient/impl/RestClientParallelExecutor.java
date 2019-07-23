package io.github.srhojo.utils.restclient.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.srhojo.utils.restclient.RestClientExecutor;
import io.github.srhojo.utils.restclient.client.RestClient;
import io.github.srhojo.utils.restclient.entities.RestRequest;

/**
 * <pre>
 * Builder implementation of {@link RestClientExecutor}.
 * That's a complex implementation that execute 'N' numbers of Request using
 * {@link Stream#parallel()} an a {@link RestClientBuilderExecutor}.
 *
 * {@link #execute()} returns a list of responses.
 * </pre>
 *
 * ATTENTION: All response <b>MUST</b> be the same responseType or Void
 *
 * @author: srhojo
 * @see <a href="https://github.com/srhojo">GitHub</a>
 */
public class RestClientParallelExecutor implements RestClientExecutor {

    private final RestClient restClient;
    private final List<RestRequest> restRequests;

    /**
     * Private constructor
     *
     * @param restClient RestClient to execute
     */
    private RestClientParallelExecutor(final RestClient restClient) {
        this.restClient = restClient;
        this.restRequests = new ArrayList<>();
    }

    /**
     * Static point of entry to create RestClientParallelExecutor
     *
     * @param restClient an implementation of RestClient
     * @return A RestClientParallelExecutor
     */
    public static RestClientParallelExecutor of(final RestClient restClient) {
        return new RestClientParallelExecutor(restClient);
    }

    /**
     * Method to add a RestRequest to the request list
     *
     * @param restRequest RestRequest to add in the execution
     * @return This RestClientParallelExecutor
     */
    public RestClientParallelExecutor addRequest(final RestRequest restRequest) {
        this.restRequests.add(restRequest);
        return this;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T execute() {
        final Stream<RestClientBuilderExecutor> restClientBuilderExecutorStream = restRequests.stream()
                .map(this::createBuilderExecutor).collect(Collectors.toList()).stream();
        return (T) restClientBuilderExecutorStream.parallel().map(RestClientBuilderExecutor::execute)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Private method to constructor a builder Executor from a RestRequest using the
     * RestClient.
     *
     * @param restRequest request
     * @return A RestClientBuilderExecutor
     */
    private RestClientBuilderExecutor createBuilderExecutor(final RestRequest restRequest) {
        final RestClientBuilderExecutor executor = RestClientBuilderExecutor.of(restClient).url(restRequest.getUrl())
                .method(restRequest.getHttpMethod()).withQueryParams(restRequest.getQueryParams())
                .withHeaders(restRequest.getHeaders());

        if (restRequest.getRequest() != null) {
            executor.withRequest(restRequest.getRequest());
        }
        if (restRequest.getResponseType() != null) {
            executor.withResponseType(restRequest.getResponseType());
        }

        return executor;
    }

}
