package org.kiwi.spring.reactive.customer.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kiwi.spring.reactive.core.exception.BusinessException;
import org.kiwi.spring.reactive.core.exception.ErrorDO;
import org.kiwi.spring.reactive.core.exception.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Component
public class CustomerResourceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerResourceClient.class);
    private static final String ERROR_UNABLE_PROCESS_JSON_CODE = "10000";
    private static final String ERROR_UNABLE_PROCESS_JSON_MESSAGE = "Unable to process JSON Mapping";

    @Value("${backend.client.customerResourceUrl}")
    private String customerResourceUrl;

    private WebClient webClient;
    private ObjectMapper objectMapper;

    public CustomerResourceClient(WebClient webclient, ObjectMapper objectMapper) {
        this.webClient = webclient;
        this.objectMapper = objectMapper;
    }

    public Mono<CustomerDO> getCustomerById(String customerId) {
        return this.webClient
                .get().uri(customerResourceUrl + "/{id}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .attributes(clientRegistrationId("okta"))
                .retrieve()
                // Handle HTTP 4XX Errors
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new TechnicalException(
                                String.valueOf(clientResponse.statusCode().value()),
                                clientResponse.statusCode().toString())))
                // Handle HTTP 5XX Errors
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    // Handle HTTP 500 Error
                    if (HttpStatus.INTERNAL_SERVER_ERROR == clientResponse.statusCode()) {
                        return clientResponse.bodyToMono(ErrorDO.class).flatMap(errorDO ->
                                Mono.error(new BusinessException(
                                        errorDO.getErrorCode(),
                                        errorDO.getErrorMessage())));
                    } else {
                        return Mono.error(new TechnicalException(
                                String.valueOf(clientResponse.statusCode().value()),
                                clientResponse.statusCode().toString()));
                    }
                })
                // Handle Success Response
                .bodyToMono(JsonNode.class)
                .doOnNext(responseNode -> LOGGER.debug("getCustomerById: response={}", responseNode))
                .flatMap(responseNode -> {
                    CustomerDO customerDO;
                    try {
                        customerDO = objectMapper.treeToValue(responseNode.get("data"), CustomerDO.class);
                    } catch (JsonProcessingException e) {
                        return Mono.error(new TechnicalException(
                                ERROR_UNABLE_PROCESS_JSON_CODE,
                                ERROR_UNABLE_PROCESS_JSON_MESSAGE));
                    }
                    return Mono.just(customerDO);
                });
    }

    public Mono<List<CustomerDO>> getCustomers() {
        return this.webClient
                .get().uri(customerResourceUrl)
                .accept(MediaType.APPLICATION_JSON)
                .attributes(clientRegistrationId("okta"))
                .retrieve()
                // Handle HTTP 4XX Errors
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new TechnicalException(
                                String.valueOf(clientResponse.statusCode().value()),
                                clientResponse.statusCode().toString())))
                // Handle HTTP 5XX Errors
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    // Handle HTTP 500 Error
                    if (HttpStatus.INTERNAL_SERVER_ERROR == clientResponse.statusCode()) {
                        return clientResponse.bodyToMono(ErrorDO.class).flatMap(errorDO ->
                                Mono.error(new BusinessException(
                                        errorDO.getErrorCode(),
                                        errorDO.getErrorMessage())));
                    } else {
                        return Mono.error(new TechnicalException(
                                String.valueOf(clientResponse.statusCode().value()),
                                clientResponse.statusCode().toString()));
                    }
                })
                // Handle Success Response
                .bodyToMono(JsonNode.class)
                .doOnNext(responseNode -> LOGGER.debug("getCustomerById: response={}", responseNode))
                .flatMap(responseNode -> {
                    List<CustomerDO> customerDOs;
                    try {
                        customerDOs = Arrays.asList(
                                objectMapper.treeToValue(responseNode.get("data"), CustomerDO[].class));
                    } catch (JsonProcessingException e) {
                        return Mono.error(new TechnicalException(
                                ERROR_UNABLE_PROCESS_JSON_CODE,
                                ERROR_UNABLE_PROCESS_JSON_MESSAGE));
                    }
                    return Mono.just(customerDOs);
                });
    }

}
