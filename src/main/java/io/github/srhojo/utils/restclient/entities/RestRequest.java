package io.github.srhojo.utils.restclient.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;

/**
 *
 * @author: srhojo
 * @see <a href="https://github.com/srhojo">GitHub</a>
 *
 */
public class RestRequest {

    // Required parameters
    private String url;
    private HttpMethod httpMethod;

    // Optional parameters
    private Class<?> responseType;
    private Object request;
    private final List<NameValuePair> queryParams;
    private final List<NameValuePair> headers;

    public RestRequest() {
        this.headers = new ArrayList<>();
        this.queryParams = new ArrayList<>();
    }

    public RestRequest(final String url, final HttpMethod httpMethod) {
        this();
        this.url = url;
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(final HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Class<?> getResponseType() {
        return responseType;
    }

    public void setResponseType(final Class<?> responseType) {
        this.responseType = responseType;
    }

    public Object getRequest() {
        return request;
    }

    public void setRequest(final Object request) {
        this.request = request;
    }

    public List<NameValuePair> getQueryParams() {
        return queryParams;
    }

    public List<NameValuePair> getHeaders() {
        return headers;
    }
}
