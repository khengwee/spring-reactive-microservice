package org.kiwi.spring.reactive.core.exception;

public class BusinessException extends RuntimeException {
    private final String code;

    public BusinessException(String code) {
        super();
        this.code = code;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
