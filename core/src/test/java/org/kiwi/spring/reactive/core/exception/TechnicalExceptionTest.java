package org.kiwi.spring.reactive.core.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class TechnicalExceptionTest {

    @Test(expected = TechnicalException.class)
    public void testConstructorCode() {
        TechnicalException technicalException = new TechnicalException("100");
        assertEquals("100", technicalException.getCode());
        throw technicalException;
    }

    @Test(expected = TechnicalException.class)
    public void testConstructorCodeAndMessageAndException() {
        TechnicalException technicalException = new TechnicalException(
                "100", "Business Exception 100", new RuntimeException());
        assertEquals("100", technicalException.getCode());
        assertEquals("Business Exception 100", technicalException.getMessage());
        throw technicalException;
    }

    @Test(expected = TechnicalException.class)
    public void testConstructorCodeAndMessage() {
        TechnicalException technicalException = new TechnicalException(
                "100", "Business Exception 100");
        assertEquals("100", technicalException.getCode());
        assertEquals("Business Exception 100", technicalException.getMessage());
        throw technicalException;
    }

    @Test(expected = TechnicalException.class)
    public void testConstructorCodeAndException() {
        TechnicalException technicalException = new TechnicalException(
                "100", new RuntimeException());
        assertEquals("100", technicalException.getCode());
        throw technicalException;
    }
}
