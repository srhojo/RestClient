package com.srhojo.utils.restclient.entities;

import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Author: srhojo
 * URL: https://github.com/srhojo
 */
public class RestRequest {

    //Required parameters
    private String url;
    private HttpMethod httpMethod;

    //Optional parameters
    private Optional<Class<?>> responseType;
    private Optional<?> request;
    private final List<NameValuePair> queryParams;
    private final List<NameValuePair> headers;


    public RestRequest() {
        this.request = Optional.empty();
        this.responseType = Optional.empty();
        this.headers = new ArrayList<>();
        this.queryParams = new ArrayList<>();
    }

    public RestRequest(String url, HttpMethod httpMethod) {
        this();
        this.url = url;
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Optional<Class<?>> getResponseType() {
        return responseType;
    }

    public void setResponseType(Optional<Class<?>> responseType) {
        this.responseType = responseType;
    }

    public Optional<?> getRequest() {
        return request;
    }

    public void setRequest(Optional<?> request) {
        this.request = request;
    }

    public List<NameValuePair> getQueryParams() {
        return queryParams;
    }

    public List<NameValuePair> getHeaders() {
        return headers;
    }
}
