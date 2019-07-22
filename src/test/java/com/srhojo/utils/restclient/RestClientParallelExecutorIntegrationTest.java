package com.srhojo.utils.restclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.srhojo.utils.restclient.entities.RestRequest;
import com.srhojo.utils.restclient.entities.TestEntity;
import com.srhojo.utils.restclient.impl.RestClientParallelExecutor;

/**
 * @author: srhojo
 * @see <a href="https://github.com/srhojo">GitHub</a>
 *
 */
@RunWith(SpringRunner.class)
@SpringJUnitConfig(classes = RestClientConfiguration.class)
public class RestClientParallelExecutorIntegrationTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private RestClient restClient;

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void test() {
        // Given
        final RestRequest request1 = new RestRequest("http://uri.com/api/foo", HttpMethod.GET);
        request1.setResponseType(TestEntity.class);

        final RestRequest request2 = new RestRequest("http://uri.com/api/foo", HttpMethod.POST);
        request2.setResponseType(TestEntity.class);
        request2.setRequest(new TestEntity());

        final RestRequest request3 = new RestRequest("http://uri.com/api/foo/1", HttpMethod.DELETE);

        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity(new TestEntity(), HttpStatus.OK));
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity(new TestEntity(), HttpStatus.OK));
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.DELETE), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity(HttpStatus.OK));

        // When
        final List<TestEntity> responseList = RestClientParallelExecutor.of(restClient).addRequest(request1)
                .addRequest(request2).addRequest(request3).execute();

        // Then
        verify(restTemplate, times(1)).exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
                any(Class.class));
        verify(restTemplate, times(1)).exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class),
                any(Class.class));
        verify(restTemplate, times(1)).exchange(any(String.class), eq(HttpMethod.DELETE), any(HttpEntity.class),
                any(Class.class));
        assertNotNull(responseList);
        assertEquals(2, responseList.size());

    }

}
