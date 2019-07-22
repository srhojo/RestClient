package com.srhojo.utils.restclient.aop;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.srhojo.utils.restclient.entities.NameValuePair;
import com.srhojo.utils.restclient.entities.RestRequest;
import com.srhojo.utils.restclient.entities.TestEntity;
import com.srhojo.utils.restclient.exceptions.RestClientException;

/**
 * @author: srhojo
 * @see <a href="https://github.com/srhojo">GitHub</a>
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class RestClientLoggerAopTest {

    @InjectMocks
    private RestClientLoggerAop restClientLoggerAop;

    /**
     * Test method for
     * {@link com.srhojo.utils.restclient.aop.RestClientLoggerAop#markedAsLogged()}.
     */
    @Test
    public void testMarkedAsLogged() {
        restClientLoggerAop.markedAsLogged();
    }

    /**
     * Test method for
     * {@link com.srhojo.utils.restclient.aop.RestClientLoggerAop#logBefore(org.aspectj.lang.JoinPoint)}.
     */
    @Test
    public void testLogBefore() {
        // Given
        final JoinPoint jp = Mockito.mock(JoinPoint.class);
        final Signature s = Mockito.mock(Signature.class);
        when(jp.getSignature()).thenReturn(s);

        // When
        restClientLoggerAop.logBefore(jp);

        // Then
        verify(jp, times(1)).getSignature();
    }

    /**
     * Test method for
     * {@link com.srhojo.utils.restclient.aop.RestClientLoggerAop#executionAround(org.aspectj.lang.ProceedingJoinPoint)}.
     *
     * @throws Throwable
     */
    @Test
    public void testExecutionAround() throws Throwable {
        // Given
        final ProceedingJoinPoint pjp = Mockito.mock(ProceedingJoinPoint.class);

        final RestRequest restRequest = new RestRequest("http://uri.com/api/foo", HttpMethod.GET);
        final TestEntity request = new TestEntity();
        request.setTest("testOne");
        restRequest.setRequest(request);
        restRequest.setResponseType(TestEntity.class);

        final Object[] args = { restRequest };

        when(pjp.getArgs()).thenReturn(args);
        when(pjp.proceed()).thenReturn(new TestEntity());

        // When
        final Object response = restClientLoggerAop.executionAround(pjp);

        // Then
        verify(pjp, times(1)).getArgs();
        verify(pjp, times(1)).proceed();
        assertNotNull(response);
    }

    @Test
    public void testExecutionAround_withoutResponseAndRequest() throws Throwable {
        // Given
        final ProceedingJoinPoint pjp = Mockito.mock(ProceedingJoinPoint.class);

        final RestRequest restRequest = new RestRequest("http://uri.com/api/foo", HttpMethod.GET);

        final Object[] args = { restRequest };

        when(pjp.getArgs()).thenReturn(args);
        when(pjp.proceed()).thenReturn(new TestEntity());

        // When
        final Object response = restClientLoggerAop.executionAround(pjp);

        // Then
        verify(pjp, times(1)).getArgs();
        verify(pjp, times(1)).proceed();
        assertNotNull(response);
    }

    @Test
    public void testExecutionAround_withHeadersAndQueryParams() throws Throwable {
        // Given
        final ProceedingJoinPoint pjp = Mockito.mock(ProceedingJoinPoint.class);

        final RestRequest restRequest = new RestRequest("http://uri.com/api/foo", HttpMethod.GET);
        final TestEntity request = new TestEntity();
        request.setTest("testOne");
        restRequest.setRequest(request);
        restRequest.setResponseType(TestEntity.class);

        final List<NameValuePair> customHeaders = new ArrayList<>();
        customHeaders.add(NameValuePair.of("headerOne", "value1"));
        customHeaders.add(NameValuePair.of("headerTwo", "value2"));

        final List<NameValuePair> queryParams = new ArrayList<>();
        queryParams.add(NameValuePair.of("queryParamOne", "qValue1"));
        queryParams.add(NameValuePair.of("queryParamTwo", "qValue2"));

        restRequest.getHeaders(); // añadir cabeceras
        restRequest.getQueryParams(); // añadir queryParams

        final Object[] args = { restRequest };

        when(pjp.getArgs()).thenReturn(args);
        when(pjp.proceed()).thenReturn(new TestEntity());

        // When
        final Object response = restClientLoggerAop.executionAround(pjp);

        // Then
        verify(pjp, times(1)).getArgs();
        verify(pjp, times(1)).proceed();
        assertNotNull(response);
    }

    @Test(expected = RestClientException.class)
    public void testExecutionAround_restClientException() throws Throwable {
        // Given
        final ProceedingJoinPoint pjp = Mockito.mock(ProceedingJoinPoint.class);
        final RestRequest restRequest = new RestRequest("http://uri.com/api/foo", HttpMethod.GET);
        final Object[] args = { restRequest };

        when(pjp.getArgs()).thenReturn(args);
        when(pjp.proceed()).thenThrow(new RestClientException(HttpStatus.INTERNAL_SERVER_ERROR, "RCE-T001", "Test"));

        // When
        restClientLoggerAop.executionAround(pjp);

        // Then
        // Exception

    }

}
