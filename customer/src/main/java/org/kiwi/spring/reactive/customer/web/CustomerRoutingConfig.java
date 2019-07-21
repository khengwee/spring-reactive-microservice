package org.kiwi.spring.reactive.customer.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class CustomerRoutingConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public RouterFunction<ServerResponse> customerRoutes(CustomerHandler customerHandler) {
        return RouterFunctions.route().path(contextPath, builder -> builder
                .GET("/customers/{customerId}", accept(MediaType.APPLICATION_JSON), customerHandler::findOne)
                .GET("/customers", accept(MediaType.APPLICATION_JSON), customerHandler::findAll)
        ).build();
    }
}
