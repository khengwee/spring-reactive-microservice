package org.kiwi.spring.reactive.core.exception;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ErrorAttributesTest {

    @Autowired
    private ErrorAttributes errorAttributes;

    private List<HttpMessageReader<?>> readers = ServerCodecConfigurer.create().getReaders();

    @Before
    public void setUp() {
    }

    @Test
    public void testGetErrorAttributesBusinessException() {
        BusinessException businessException = new BusinessException("100", "Business Error 100");
        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        Map<String, Object> errorAttributeMap = errorAttributes.getErrorAttributes(
                buildServerRequest(request, businessException), false);
        ErrorDO errorDO = (ErrorDO) errorAttributeMap.get(ExceptionConstant.ERROR_ATTRIBUTE_ERROR_KEY);
        assertEquals("100", errorDO.getErrorCode());
        assertEquals("Business Error 100", errorDO.getErrorMessage());
    }

    @Test
    public void testGetErrorAttributesTechnicalException() {
        TechnicalException technicalException = new TechnicalException("200", "Technical Error 200");
        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        Map<String, Object> errorAttributeMap = errorAttributes.getErrorAttributes(
                buildServerRequest(request, technicalException), false);
        ErrorDO errorDO = (ErrorDO) errorAttributeMap.get(ExceptionConstant.ERROR_ATTRIBUTE_ERROR_KEY);
        assertEquals("200", errorDO.getErrorCode());
        assertEquals("Technical Error 200", errorDO.getErrorMessage());
    }

    @Test
    public void testGetErrorAttributesOtherException() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        Map<String, Object> errorAttributeMap = errorAttributes.getErrorAttributes(
                buildServerRequest(request, new NullPointerException()), false);
        ErrorDO errorDO = (ErrorDO) errorAttributeMap.get(ExceptionConstant.ERROR_ATTRIBUTE_ERROR_KEY);
        assertEquals(ExceptionConstant.GENERAL_EXCEPTION_KEY, errorDO.getErrorCode());
        assertEquals(ExceptionConstant.GENERAL_EXCEPTION_VALUE, errorDO.getErrorMessage());
    }

    private ServerRequest buildServerRequest(MockServerHttpRequest request, Throwable error) {
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        errorAttributes.storeErrorInformation(error, exchange);
        return ServerRequest.create(exchange, readers);
    }
}
