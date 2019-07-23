package io.github.srhojo.utils.restclient.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;

import com.srhojo.utils.restclient.client.RestClient;
import com.srhojo.utils.restclient.entities.RestRequest;
import com.srhojo.utils.restclient.impl.RestClientParallelExecutor;

import io.github.srhojo.utils.restclient.entities.TestEntity;

/**
 * @author: srhojo
 * @see <a href="https://github.com/srhojo">GitHub</a>
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class RestClientParallelExecutorTest {

    @Mock
    private RestClient restClient;

    /**
     * Test method for
     * {@link com.srhojo.utils.restclient.impl.RestClientParallelExecutor#execute()}.
     */
    @Test
    public void execute() {
        // Given
        final RestRequest request1 = new RestRequest("http://uri.com/api/1", HttpMethod.GET);
        final RestRequest request2 = new RestRequest("http://uri.com/api/1", HttpMethod.GET);
        final RestRequest request3 = new RestRequest("http://uri.com/api/1", HttpMethod.GET);
        request3.setRequest(new TestEntity());
        request3.setResponseType(TestEntity.class);

        final RestClientParallelExecutor restClientParallelExecutor = RestClientParallelExecutor.of(restClient)
                .addRequest(request1).addRequest(request2).addRequest(request3);

        when(restClient.execute(any(RestRequest.class))).thenReturn(new TestEntity());

        // When
        final List<?> response = restClientParallelExecutor.execute();

        // Then
        assertNotNull(response);
        verify(restClient, times(3)).execute(any(RestRequest.class));

    }

}
