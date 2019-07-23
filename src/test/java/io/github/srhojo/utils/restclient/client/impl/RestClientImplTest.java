package io.github.srhojo.utils.restclient.client.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.srhojo.utils.restclient.client.impl.RestClientImpl;
import com.srhojo.utils.restclient.entities.NameValuePair;
import com.srhojo.utils.restclient.entities.RestRequest;
import com.srhojo.utils.restclient.exceptions.RestClientException;

import io.github.srhojo.utils.restclient.entities.TestEntity;

/**
 * @author: srhojo
 * @see <a href="https://github.com/srhojo">GitHub</a>
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class RestClientImplTest {

    @InjectMocks
    private RestClientImpl restClient;

    @Mock
    private RestTemplate restTemplate;

    @Captor
    private ArgumentCaptor<String> uriBuilder;

    @Captor
    private ArgumentCaptor<HttpEntity<?>> entity;

    @Captor
    private ArgumentCaptor<Class<?>> responseClass;

    /**
     * Test method for
     * {@link com.srhojo.utils.restclient.client.impl.RestClientImpl#execute(com.srhojo.utils.restclient.entities.RestRequest)}.
     */
    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void execute_GET() {
        // Given
        final RestRequest restRequest = new RestRequest("http://test.com/api", HttpMethod.GET);
        restRequest.getQueryParams().add(NameValuePair.of("param1", "value1"));
        restRequest.getQueryParams().add(NameValuePair.of("param2", "value2"));
        restRequest.setResponseType(TestEntity.class);

        when(restTemplate.exchange(any(String.class), eq(restRequest.getHttpMethod()), any(), any(Class.class)))
                .thenReturn(new ResponseEntity(new TestEntity(), HttpStatus.OK));

        // When
        final TestEntity response = restClient.execute(restRequest);

        // Then
        verify(restTemplate, times(1)).exchange(uriBuilder.capture(), eq(restRequest.getHttpMethod()), entity.capture(),
                responseClass.capture());

        assertNotNull(response);
        assertNull(entity.getValue().getBody());
        assertEquals("application/json", entity.getValue().getHeaders().get("Accept").get(0));
        assertEquals("application/json", entity.getValue().getHeaders().get("Content-Type").get(0));
        assertEquals(restRequest.getResponseType(), responseClass.getValue());
        assertEquals("http://test.com/api?param1=value1&param2=value2", uriBuilder.getValue());

    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void execute_POST() {
        // Given
        final RestRequest restRequest = new RestRequest("http://test.com/api", HttpMethod.POST);
        final TestEntity request = new TestEntity();
        request.setTest("testOne");
        restRequest.setRequest(request);
        restRequest.setResponseType(TestEntity.class);

        final List<NameValuePair> customHeaders = new ArrayList<>();
        customHeaders.add(NameValuePair.of("headerOne", "one"));
        customHeaders.add(NameValuePair.of("headerTwo", "two"));

        restRequest.getHeaders().addAll(customHeaders);

        when(restTemplate.exchange(any(String.class), eq(restRequest.getHttpMethod()), any(), any(Class.class)))
                .thenReturn(new ResponseEntity(new TestEntity(), HttpStatus.OK));

        // When
        final TestEntity response = restClient.execute(restRequest);

        // Then
        verify(restTemplate, times(1)).exchange(uriBuilder.capture(), eq(restRequest.getHttpMethod()), entity.capture(),
                responseClass.capture());

        assertNotNull(response);
        assertEquals(entity.getValue().getBody(), request);
        assertEquals("one", entity.getValue().getHeaders().get("headerOne").get(0));
        assertEquals("two", entity.getValue().getHeaders().get("headerTwo").get(0));
        assertEquals(restRequest.getResponseType(), responseClass.getValue());
        assertEquals("http://test.com/api", uriBuilder.getValue());
    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void execute_responseVoid() {
        // Given
        final RestRequest restRequest = new RestRequest("http://test.com/api/foo/1", HttpMethod.DELETE);

        when(restTemplate.exchange(any(String.class), eq(restRequest.getHttpMethod()), any(), any(Class.class)))
                .thenReturn(new ResponseEntity(new TestEntity(), HttpStatus.OK));

        // When
        final TestEntity response = restClient.execute(restRequest);

        // Then
        verify(restTemplate, times(1)).exchange(uriBuilder.capture(), eq(restRequest.getHttpMethod()), entity.capture(),
                responseClass.capture());
        assertNotNull(response);
        assertNull(entity.getValue().getBody());
        assertNotNull(entity.getValue().getHeaders());
        assertEquals(responseClass.getValue(), Void.class);
        assertEquals("http://test.com/api/foo/1", uriBuilder.getValue());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RestClientException.class)
    public void execute_RestClientResponseException() {
        // Given
        final RestRequest restRequest = new RestRequest("http://test.com/api/foo/1", HttpMethod.DELETE);

        when(restTemplate.exchange(any(String.class), eq(restRequest.getHttpMethod()), any(), any(Class.class)))
                .thenThrow(new RestClientResponseException("Error", 404, "NOT FOUND", null, null, null));

        try {
            // When
            restClient.execute(restRequest);

        } catch (final RestClientException re) {
            // Then
            assertEquals("RC-E001", re.getCode());
            assertEquals(HttpStatus.NOT_FOUND, re.getStatus());
            assertNotNull(re.getDetails());
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RestClientException.class)
    public void execute_genericException() {
        // Given
        final RestRequest restRequest = new RestRequest();
        restRequest.setUrl("http://test.com/api/foo/1");
        restRequest.setHttpMethod(HttpMethod.DELETE);

        when(restTemplate.exchange(any(String.class), eq(restRequest.getHttpMethod()), any(), any(Class.class)))
                .thenThrow(new RuntimeException("Generic exception"));

        try {
            // When
            restClient.execute(restRequest);

        } catch (final RestClientException re) {
            // Then
            assertEquals("RC-E002", re.getCode());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, re.getStatus());
            assertNotNull(re.getDetails());
            throw re;
        }
    }

}
