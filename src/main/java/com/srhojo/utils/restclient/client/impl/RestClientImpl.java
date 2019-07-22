package com.srhojo.utils.restclient.client.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.srhojo.utils.restclient.aop.RestClientLogger;
import com.srhojo.utils.restclient.client.RestClient;
import com.srhojo.utils.restclient.entities.NameValuePair;
import com.srhojo.utils.restclient.entities.RestRequest;
import com.srhojo.utils.restclient.exceptions.RestClientException;

/**
 *
 * @author: srhojo
 * @see <a href="https://github.com/srhojo">GitHub</a>
 *
 */
@Component
@RestClientLogger
public class RestClientImpl implements RestClient {

    private static final String CONST_HANDLED_EXCEPTION = "RC-E001";
    private static final String CONST_UNHANDLED_EXCEPTION = "RC-E002";

    private final RestTemplate restTemplate;

    public RestClientImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     *
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T execute(final RestRequest restRequest) {
        try {
            final HttpHeaders headers = !restRequest.getHeaders().isEmpty() ? createHeaders(restRequest.getHeaders())
                    : createDefaultHeader();

            @SuppressWarnings("rawtypes")
            final HttpEntity entity = restRequest.getRequest() != null
                    ? new HttpEntity<>(restRequest.getRequest(), headers)
                    : new HttpEntity<>(headers);

            final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(restRequest.getUrl());
            restRequest.getQueryParams()
                    .forEach(nameValue -> uriBuilder.queryParam(nameValue.getName(), nameValue.getValue()));

            final Class<?> responseClass = restRequest.getResponseType() != null ? restRequest.getResponseType()
                    : Void.class;

            return (T) restTemplate
                    .exchange(uriBuilder.toUriString(), restRequest.getHttpMethod(), entity, responseClass).getBody();
        } catch (final RestClientResponseException re) {
            throw new RestClientException(HttpStatus.valueOf(re.getRawStatusCode()), CONST_HANDLED_EXCEPTION, re);
        } catch (final Exception e) {
            throw new RestClientException(HttpStatus.INTERNAL_SERVER_ERROR, CONST_UNHANDLED_EXCEPTION, e.getMessage());
        }
    }

    private HttpHeaders createHeaders(final List<NameValuePair> customHeaders) {
        final HttpHeaders headers = new HttpHeaders();
        customHeaders.forEach(nameValue -> headers.set(nameValue.getName(), nameValue.getValue()));
        return headers;
    }

    private HttpHeaders createDefaultHeader() {
        final HttpHeaders baseHeader = new HttpHeaders();
        baseHeader.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        baseHeader.setContentType(MediaType.APPLICATION_JSON);
        return baseHeader;
    }

}
