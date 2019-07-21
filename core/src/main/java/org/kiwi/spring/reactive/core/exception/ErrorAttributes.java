package org.kiwi.spring.reactive.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class ErrorAttributes extends DefaultErrorAttributes {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorAttributes.class);

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Map<String, Object> errorAttributeMap = super.getErrorAttributes(request, includeStackTrace);
        Throwable exception = getError(request);
        LOGGER.error("{} Error StackTrace: ", exception.getClass().getSimpleName(), exception);
        ErrorDO errorDO = new ErrorDO();
        if (exception instanceof BusinessException) {
            BusinessException businessException = (BusinessException) exception;
            errorAttributeMap.put(ExceptionConstant.ERROR_ATTRIBUTE_HTTP_STATUS_KEY,
                    HttpStatus.UNPROCESSABLE_ENTITY);
            errorDO.setErrorCode(businessException.getCode());
            errorDO.setErrorMessage(businessException.getMessage());
        } else if (exception instanceof TechnicalException) {
            TechnicalException technicalException = (TechnicalException) exception;
            errorAttributeMap.put(ExceptionConstant.ERROR_ATTRIBUTE_HTTP_STATUS_KEY,
                    HttpStatus.INTERNAL_SERVER_ERROR);
            errorDO.setErrorCode(technicalException.getCode());
            errorDO.setErrorMessage(technicalException.getMessage());
        } else {
            errorAttributeMap.put(ExceptionConstant.ERROR_ATTRIBUTE_HTTP_STATUS_KEY,
                    HttpStatus.INTERNAL_SERVER_ERROR);
            errorDO.setErrorCode(ExceptionConstant.GENERAL_EXCEPTION_KEY);
            errorDO.setErrorMessage(ExceptionConstant.GENERAL_EXCEPTION_VALUE);
        }
        errorAttributeMap.put(ExceptionConstant.ERROR_ATTRIBUTE_ERROR_KEY, errorDO);
        return errorAttributeMap;
    }
}
