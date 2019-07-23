package com.srhojo.utils.restclient.impl;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.http.HttpMethod;

import com.srhojo.utils.restclient.RestClientExecutor;
import com.srhojo.utils.restclient.client.RestClient;
import com.srhojo.utils.restclient.entities.NameValuePair;
import com.srhojo.utils.restclient.entities.RestRequest;

/**
 * Builder implementation of {@link RestClientExecutor}. That's a base
 * implementation who return a simple {@link #withResponseType(Class)}
 *
 * @author: srhojo
 * @see <a href="https://github.com/srhojo">GitHub</a>
 *
 */
public class RestClientBuilderExecutor implements RestClientExecutor {

    private final RestClient restClient;
    private final RestRequest restRequest;

    private RestClientBuilderExecutor(final RestClient restClient) {
        this.restClient = restClient;
        this.restRequest = new RestRequest();
    }

    /**
     * Static point of entry to create RestClientBuilderExecutor
     *
     * @param restClient RestClient to execute
     * @return A RestClientBuilderExecutor
     */
    public static RestClientBuilderExecutor of(@NotNull final RestClient restClient) {
        return new RestClientBuilderExecutor(restClient);
    }

    /**
     * Method to set URL at the request
     *
     * @param url Url to set
     * @return This RestClientBuilderExecutor
     */
    public RestClientBuilderExecutor url(@NotNull final String url) {
        restRequest.setUrl(url);
        return this;
    }

    /**
     * Method to set HttpMethod at the request
     *
     * @param method HttpMethod to set
     * @return This RestClientBuilderExecutor
     */
    public RestClientBuilderExecutor method(@NotNull final HttpMethod method) {
        restRequest.setHttpMethod(method);
        return this;
    }

    /**
     * Method to set Object at the request
     *
     * @param request Object to set
     * @return This RestClientBuilderExecutor
     */
    public RestClientBuilderExecutor withRequest(@NotNull final Object request) {
        restRequest.setRequest(Optional.of(request));
        return this;
    }

    /**
     * Method to set ResponseType at the request
     *
     * @param responseType ResponseType Class to set
     * @return This RestClientBuilderExecutor
     */
    public RestClientBuilderExecutor withResponseType(@NotNull final Class<?> responseType) {
        restRequest.setResponseType(responseType);
        return this;
    }

    /**
     * Method to set custom Headers at the request
     *
     * @param headers A {@link NameValuePair} list with headers to set.
     * @return This RestClientBuilderExecutor
     */
    public RestClientBuilderExecutor withHeaders(final List<NameValuePair> headers) {
        restRequest.getHeaders().addAll(headers);
        return this;
    }

    /**
     * Method to set QueryParams at the request
     *
     * @param queryParams A {@link NameValuePair} list with query params to set
     * @return This RestClientBuilderExecutor
     */
    public RestClientBuilderExecutor withQueryParams(final List<NameValuePair> queryParams) {
        restRequest.getQueryParams().addAll(queryParams);
        return this;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public <T> T execute() {
        return restClient.execute(restRequest);
    }
}
