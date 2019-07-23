package io.github.srhojo.utils.restclient.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;

import com.srhojo.utils.restclient.client.RestClient;
import com.srhojo.utils.restclient.entities.NameValuePair;
import com.srhojo.utils.restclient.entities.RestRequest;
import com.srhojo.utils.restclient.impl.RestClientBuilderExecutor;

import io.github.srhojo.utils.restclient.entities.TestEntity;

/**
 * @author: srhojo
 * @see <a href="https://github.com/srhojo">GitHub</a>
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class RestClientBuilderExecutorTest {

    @Mock
    private RestClient restClient;

    /**
     * Test method for
     * {@link com.srhojo.utils.restclient.impl.RestClientBuilderExecutor#execute()}.
     */
    @Test
    public void execute() {
        // Given
        final List<NameValuePair> headers = new ArrayList<>();
        final ArrayList<NameValuePair> queryParams = new ArrayList<>();

        final RestClientBuilderExecutor restClientBuilderExecutor = RestClientBuilderExecutor.of(restClient)
                .method(HttpMethod.GET).url("http://uri.com/api").withRequest(new TestEntity())
                .withResponseType(TestEntity.class).withHeaders(headers).withQueryParams(queryParams);

        when(restClient.execute(any(RestRequest.class))).thenReturn(new TestEntity());

        // When
        final TestEntity response = restClientBuilderExecutor.execute();

        // Then
        assertNotNull(response);
        verify(restClient, times(1)).execute(any(RestRequest.class));
    }

}
