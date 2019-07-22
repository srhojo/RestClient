package com.srhojo.utils.restclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.srhojo.utils.restclient.client.RestClient;
import com.srhojo.utils.restclient.entities.NameValuePair;
import com.srhojo.utils.restclient.entities.TestEntity;
import com.srhojo.utils.restclient.impl.RestClientBuilderExecutor;

/**
 * @author: srhojo
 * @see <a href="https://github.com/srhojo">GitHub</a>
 *
 */
@RunWith(SpringRunner.class)
@SpringJUnitConfig(classes = RestClientConfiguration.class)
public class RestClientBuilderExecutorIntegrationTest {

    private static final String CONST_DEFAULT_ACCEPT_HEADER = "application/json";
    private static final String CONST_DEFAULT_CONTENT_TYPE_HEADER = "application/json";

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private RestClient restClient;

    @Captor
    private ArgumentCaptor<String> uriBuilder;

    @Captor
    private ArgumentCaptor<HttpMethod> httpMethod;

    @Captor
    private ArgumentCaptor<HttpEntity<?>> httpEntity;

    @Captor
    private ArgumentCaptor<Class<?>> responseClass;

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void builderExecutor_Get() {
        // Given
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity(new TestEntity(), HttpStatus.OK));

        // When
        final TestEntity testResponse = RestClientBuilderExecutor.of(restClient)
                                                                .url("http://uri.com/api/foo")
                                                                .method(HttpMethod.GET)
                                                                .withResponseType(TestEntity.class)
                                                                .execute();

        // Then
        verify(restTemplate, times(1)).exchange(uriBuilder.capture(), httpMethod.capture(), httpEntity.capture(),
                responseClass.capture());
        assertNotNull(testResponse);
        assertEquals("http://uri.com/api/foo", uriBuilder.getValue());
        assertEquals(HttpMethod.GET, httpMethod.getValue());
        assertNull(httpEntity.getValue().getBody());
        assertEquals(CONST_DEFAULT_ACCEPT_HEADER, httpEntity.getValue().getHeaders().get("Accept").get(0));
        assertEquals(CONST_DEFAULT_CONTENT_TYPE_HEADER, httpEntity.getValue().getHeaders().get("Content-Type").get(0));
        assertEquals(TestEntity.class, responseClass.getValue());
    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void builderExecutor_Get_queryParamsAndCustomHeaders() {
        // Given
        final List<NameValuePair> queryParams = new ArrayList<>();
        queryParams.add(NameValuePair.of("paramOne", "qOne"));
        queryParams.add(NameValuePair.of("paramTwo", "qTwo"));

        final List<NameValuePair> customHeaders = new ArrayList<>();
        customHeaders.add(NameValuePair.of("headerOne", "hOne"));
        customHeaders.add(NameValuePair.of("headerTwo", "hTwo"));

        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity(new TestEntity(), HttpStatus.OK));

        // When
        final TestEntity testResponse = RestClientBuilderExecutor.of(restClient)
                                                                .url("http://uri.com/api/foo")
                                                                .method(HttpMethod.GET)
                                                                .withResponseType(TestEntity.class)
                                                                .withHeaders(customHeaders)
                                                                .withQueryParams(queryParams)
                                                                .execute();

        // Then
        verify(restTemplate, times(1)).exchange(uriBuilder.capture(), httpMethod.capture(), httpEntity.capture(),
                responseClass.capture());
        assertNotNull(testResponse);
        assertEquals("http://uri.com/api/foo?paramOne=qOne&paramTwo=qTwo", uriBuilder.getValue());
        assertEquals(HttpMethod.GET, httpMethod.getValue());
        assertNull(httpEntity.getValue().getBody());
        assertEquals("hOne", httpEntity.getValue().getHeaders().get("headerOne").get(0));
        assertEquals("hTwo", httpEntity.getValue().getHeaders().get("headerTwo").get(0));
        assertEquals(TestEntity.class, responseClass.getValue());
    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void builderExecutor_POST() {
        // Given
        final TestEntity requestEntity = new TestEntity();
        requestEntity.setTest("String request test");

        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity(new TestEntity(), HttpStatus.OK));

        // When
        final TestEntity testResponse = RestClientBuilderExecutor.of(restClient)
                                                                .url("http://uri.com/api/foo")
                                                                .method(HttpMethod.POST)
                                                                .withRequest(requestEntity)
                                                                .withResponseType(TestEntity.class)
                                                                .execute();

        // Then
        verify(restTemplate, times(1)).exchange(uriBuilder.capture(), httpMethod.capture(), httpEntity.capture(),
                responseClass.capture());
        assertNotNull(testResponse);
        assertEquals("http://uri.com/api/foo", uriBuilder.getValue());
        assertEquals(HttpMethod.POST, httpMethod.getValue());
        assertNotNull(httpEntity.getValue().getBody());
        assertEquals(requestEntity, ((Optional<TestEntity>) httpEntity.getValue().getBody()).get());
        assertEquals(CONST_DEFAULT_ACCEPT_HEADER, httpEntity.getValue().getHeaders().get("Accept").get(0));
        assertEquals(CONST_DEFAULT_CONTENT_TYPE_HEADER, httpEntity.getValue().getHeaders().get("Content-Type").get(0));
        assertEquals(TestEntity.class, responseClass.getValue());
    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void builderExecutor_PUT_withNotResponse() {
        // Given
        final TestEntity requestEntity = new TestEntity();
        requestEntity.setTest("String request test");

        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity(HttpStatus.OK));

        // When
        final TestEntity testResponse = RestClientBuilderExecutor.of(restClient)
                                                                .url("http://uri.com/api/foo")
                                                                .method(HttpMethod.PUT)
                                                                .withRequest(requestEntity)
                                                                .execute();

        // Then
        verify(restTemplate, times(1)).exchange(uriBuilder.capture(), httpMethod.capture(), httpEntity.capture(),
                responseClass.capture());
        assertNull(testResponse);
        assertEquals("http://uri.com/api/foo", uriBuilder.getValue());
        assertEquals(HttpMethod.PUT, httpMethod.getValue());
        assertNotNull(httpEntity.getValue().getBody());
        assertEquals(requestEntity, ((Optional<TestEntity>) httpEntity.getValue().getBody()).get());
        assertEquals(CONST_DEFAULT_ACCEPT_HEADER, httpEntity.getValue().getHeaders().get("Accept").get(0));
        assertEquals(CONST_DEFAULT_CONTENT_TYPE_HEADER, httpEntity.getValue().getHeaders().get("Content-Type").get(0));

    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void builderExecutor_DELETE_withNotResponse() {
        // Given
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity(HttpStatus.OK));

        // When
        final TestEntity testResponse = RestClientBuilderExecutor.of(restClient)
                                                                .url("http://uri.com/api/foo/1")
                                                                .method(HttpMethod.DELETE)
                                                                .execute();

        // Then
        verify(restTemplate, times(1)).exchange(uriBuilder.capture(), httpMethod.capture(), httpEntity.capture(),
                responseClass.capture());
        assertNull(testResponse);
        assertEquals("http://uri.com/api/foo/1", uriBuilder.getValue());
        assertEquals(HttpMethod.DELETE, httpMethod.getValue());
        assertNull(httpEntity.getValue().getBody());
        assertEquals(CONST_DEFAULT_ACCEPT_HEADER, httpEntity.getValue().getHeaders().get("Accept").get(0));
        assertEquals(CONST_DEFAULT_CONTENT_TYPE_HEADER, httpEntity.getValue().getHeaders().get("Content-Type").get(0));
    }

}
