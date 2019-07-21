package org.kiwi.spring.reactive.account.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kiwi.spring.reactive.account.repository.AccountEntity;
import org.kiwi.spring.reactive.account.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kiwi.spring.reactive.account.web.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    private SimpleDateFormat simpleDateFormat;
    private Flux<AccountEntity> accountEntityFlux;
    private Mono<AccountEntity> accountEntityMono;

    @Before
    public void setUp() throws ParseException {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date createdDate = simpleDateFormat.parse("2018-06-08T17:00:00");
        Date updatedDate = simpleDateFormat.parse("2019-06-08T22:00:00");

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNo("12345");
        accountEntity.setAccountType("Savings Account");
        accountEntity.setAccountStatus("Active");
        accountEntity.setCreatedBy("System");
        accountEntity.setCreatedDate(createdDate);
        accountEntity.setUpdatedBy("System");
        accountEntity.setUpdatedDate(updatedDate);

        AccountEntity accountEntity2 = new AccountEntity();
        accountEntity2.setAccountNo("1234567");
        accountEntity2.setAccountType("Current Account");
        accountEntity2.setAccountStatus("Dormant");
        accountEntity2.setCreatedBy("System");
        accountEntity2.setCreatedDate(createdDate);
        accountEntity2.setUpdatedBy("System");
        accountEntity2.setUpdatedDate(updatedDate);

        accountEntityFlux = Flux.just(accountEntity).concatWithValues(accountEntity2);
        accountEntityMono = Mono.just(accountEntity);
    }

    @Test
    public void testGetAccounts() {
        when(accountRepository.findAll()).thenReturn(accountEntityFlux);
        Flux<AccountDTO> accountDTOFlux = accountService.getAccounts();
        StepVerifier.create(accountDTOFlux)
                .expectNextMatches(accountDTO -> {
                    assertEquals("12345", accountDTO.getAccountNo());
                    assertEquals("Savings Account", accountDTO.getAccountType());
                    assertEquals("Active", accountDTO.getAccountStatus());
                    return true;
                })
                .expectNextMatches(accountDTO -> {
                    assertEquals("1234567", accountDTO.getAccountNo());
                    assertEquals("Current Account", accountDTO.getAccountType());
                    assertEquals("Dormant", accountDTO.getAccountStatus());
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetAccountByAccountNo() {
        when(accountRepository.findById(anyString())).thenReturn(accountEntityMono);
        Mono<AccountDTO> accountDTOMono = accountService.getAccountByAccountNo("12345");
        StepVerifier.create(accountDTOMono)
                .expectNextMatches(accountDTO -> {
                    assertEquals("12345", accountDTO.getAccountNo());
                    assertEquals("Savings Account", accountDTO.getAccountType());
                    assertEquals("Active", accountDTO.getAccountStatus());
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void testSaveAccount() {
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntityMono);
        AccountDTO requestAccountDTO = new AccountDTO();
        requestAccountDTO.setAccountNo("12345");
        requestAccountDTO.setAccountType("Savings Account");
        requestAccountDTO.setAccountType("Active");
        Mono<AccountDTO> accountDTOMono = accountService.saveAccount(requestAccountDTO);
        StepVerifier.create(accountDTOMono)
                .expectNextMatches(accountDTO -> {
                    assertEquals("12345", accountDTO.getAccountNo());
                    assertEquals("Savings Account", accountDTO.getAccountType());
                    assertEquals("Active", accountDTO.getAccountStatus());
                    return true;
                })
                .expectComplete()
                .verify();
    }
}
