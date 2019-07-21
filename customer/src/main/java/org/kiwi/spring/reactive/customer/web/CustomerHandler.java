package org.kiwi.spring.reactive.customer.web;

import org.kiwi.spring.reactive.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CustomerHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerHandler.class);

    private final CustomerService customerService;

    public CustomerHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Mono<ServerResponse> findOne(ServerRequest request) {
        String customerId = request.pathVariable("customerId");
        // build notFound response
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return customerService.getCustomerById(customerId).flatMap(customerDTO -> {
            LOGGER.debug("findOne: customerId={}, response={}", customerId, customerDTO);
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(customerDTO));
        }).switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        // build notFound response
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
       return customerService.getCustomers().flatMap(customerDTOs -> {
           LOGGER.debug("findOne: request={}, response={}", request, customerDTOs);
           return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                   .body(BodyInserters.fromObject(customerDTOs));
       }).switchIfEmpty(notFound);

    }

}
