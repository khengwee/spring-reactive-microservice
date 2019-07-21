package org.kiwi.spring.reactive.account.service;

import org.kiwi.spring.reactive.account.repository.AccountEntity;
import org.kiwi.spring.reactive.account.repository.AccountRepository;
import org.kiwi.spring.reactive.account.mapper.AccountMapper;
import org.kiwi.spring.reactive.account.web.AccountDTO;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private static final AccountMapper MAPPER = Mappers.getMapper(AccountMapper.class);

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Flux<AccountDTO> getAccounts() {
        LOGGER.debug("getAccounts");
        return accountRepository.findAll().flatMap(accountEntity -> {
            AccountDTO accountDTO = MAPPER.toAccountDTO(accountEntity);
            return Flux.just(accountDTO);
        });
    }

    public Mono<AccountDTO> getAccountByAccountNo(String accountNo) {
        LOGGER.debug("getAccountByAccountNo: accountNo={}", accountNo);
        return accountRepository.findById(accountNo).flatMap(accountEntity -> {
            AccountDTO accountDTO = MAPPER.toAccountDTO(accountEntity);
            return Mono.just(accountDTO);
        });
    }

    public Mono<AccountDTO> saveAccount(AccountDTO accountDTO) {
        LOGGER.debug("saveAccount: accountDTO={}", accountDTO);
        AccountEntity accountEntity = MAPPER.fromAccountDTO(accountDTO);
        accountEntity.setCreatedBy("System");
        accountEntity.setCreatedDate(new Date());
        accountEntity.setUpdatedBy("System");
        accountEntity.setUpdatedDate(new Date());
        return accountRepository.save(accountEntity).flatMap(responseAccountEntity -> {
            AccountDTO responseAccountDTO = MAPPER.toAccountDTO(responseAccountEntity);
            return Mono.just(responseAccountDTO);
        });
    }
}
