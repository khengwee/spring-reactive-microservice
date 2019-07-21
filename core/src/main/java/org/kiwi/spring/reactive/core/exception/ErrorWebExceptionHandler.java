package org.kiwi.spring.reactive.core.exception;

import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Order(-2)
public class ErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public ErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                    ResourceProperties resourceProperties,
                                    ApplicationContext applicationContext,
                                    final ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resourceProperties, applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> errorAttributeMap = getErrorAttributes(request, false);
        return ServerResponse.status((HttpStatus) errorAttributeMap.get(ExceptionConstant.ERROR_ATTRIBUTE_HTTP_STATUS_KEY))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(errorAttributeMap.get(ExceptionConstant.ERROR_ATTRIBUTE_ERROR_KEY)));
    }
}
