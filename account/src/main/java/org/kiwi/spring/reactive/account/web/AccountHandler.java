package org.kiwi.spring.reactive.account.web;

import org.kiwi.spring.reactive.account.service.AccountService;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AccountHandler {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AccountHandler.class);

    private final AccountService accountService;

    public AccountHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        LOGGER.debug("findAll: request={}", request);
        Flux<AccountDTO> accountDTOFlux = accountService.getAccounts();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(accountDTOFlux, AccountDTO.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findOne(ServerRequest request) {
        String accountNo = request.pathVariable("accountNo");
        LOGGER.debug("findOne: accountNo={}", accountNo);
        return accountService.getAccountByAccountNo(accountNo)
                .flatMap(accountDTO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(accountDTO))
                        .switchIfEmpty(ServerResponse.notFound().build())
                );
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<AccountDTO> accountDTOMono = request.bodyToMono(AccountDTO.class);
        return accountDTOMono.flatMap(accountDTO -> {
            LOGGER.debug("save: accountDTO={}", accountDTO);
            return accountService.saveAccount(accountDTO)
                    .flatMap(responseAccountDTO -> ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromObject(responseAccountDTO))
                            .switchIfEmpty(ServerResponse.notFound().build())
                    );
        });
    }
}
