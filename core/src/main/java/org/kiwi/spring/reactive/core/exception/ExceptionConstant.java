package org.kiwi.spring.reactive.core.exception;

public class ExceptionConstant {

    private ExceptionConstant() {
        throw new IllegalStateException("ExceptionConstant class cannot be initialized");
    }

    public static final String ERROR_ATTRIBUTE_HTTP_STATUS_KEY = "HTTP_STATUS";
    public static final String ERROR_ATTRIBUTE_ERROR_KEY = "ERROR";
    public static final String GENERAL_EXCEPTION_KEY = "GENERAL_EXCEPTION";
    public static final String GENERAL_EXCEPTION_VALUE = "This service is temporary unavailable. Please try again later.";
}
