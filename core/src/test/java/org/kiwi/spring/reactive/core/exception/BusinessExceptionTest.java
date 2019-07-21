package org.kiwi.spring.reactive.core.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class BusinessExceptionTest {

    @Test(expected = BusinessException.class)
    public void testConstructorCode() {
        BusinessException businessException = new BusinessException("100");
        assertEquals("100", businessException.getCode());
        throw businessException;
    }

    @Test(expected = BusinessException.class)
    public void testConstructorCodeAndMessageAndException() {
        BusinessException businessException = new BusinessException(
                "100", "Business Exception 100", new RuntimeException());
        assertEquals("100", businessException.getCode());
        assertEquals("Business Exception 100", businessException.getMessage());
        throw businessException;
    }

    @Test(expected = BusinessException.class)
    public void testConstructorCodeAndMessage() {
        BusinessException businessException = new BusinessException(
                "100", "Business Exception 100");
        assertEquals("100", businessException.getCode());
        assertEquals("Business Exception 100", businessException.getMessage());
        throw businessException;
    }

    @Test(expected = BusinessException.class)
    public void testConstructorCodeAndException() {
        BusinessException businessException = new BusinessException(
                "100", new RuntimeException());
        assertEquals("100", businessException.getCode());
        throw businessException;
    }
}
