package org.kiwi.spring.reactive.account.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountRepositoryTest {

	@Autowired
	private AccountRepository accountRepository;

	private SimpleDateFormat simpleDateFormat;

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

        accountRepository.save(accountEntity).block();
        accountRepository.save(accountEntity2).block();
	}

	@Test
	public void testFindAll() throws Exception {
        Flux<AccountEntity> accountEntityFlux = accountRepository.findAll();
        Date createdDate = simpleDateFormat.parse("2018-06-08T17:00:00");
        Date updatedDate = simpleDateFormat.parse("2019-06-08T22:00:00");
        StepVerifier.create(accountEntityFlux)
                .assertNext(accountEntity -> {
                    assertEquals("12345", accountEntity.getAccountNo());
                    assertEquals("Savings Account", accountEntity.getAccountType());
                    assertEquals("Active", accountEntity.getAccountStatus());
                    assertEquals("System", accountEntity.getCreatedBy());
                    assertEquals(createdDate, accountEntity.getCreatedDate());
                    assertEquals("System", accountEntity.getUpdatedBy());
                    assertEquals(updatedDate, accountEntity.getUpdatedDate());
                })
                .assertNext(accountEntity -> {
                    assertEquals("1234567", accountEntity.getAccountNo());
                    assertEquals("Current Account", accountEntity.getAccountType());
                    assertEquals("Dormant", accountEntity.getAccountStatus());
                    assertEquals("System", accountEntity.getCreatedBy());
                    assertEquals(createdDate, accountEntity.getCreatedDate());
                    assertEquals("System", accountEntity.getUpdatedBy());
                    assertEquals(updatedDate, accountEntity.getUpdatedDate());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void testFindById() throws Exception {
        Mono<AccountEntity> accountEntityMono = accountRepository.findById("12345");
        Date createdDate = simpleDateFormat.parse("2018-06-08T17:00:00");
        Date updatedDate = simpleDateFormat.parse("2019-06-08T22:00:00");
        StepVerifier.create(accountEntityMono)
                .assertNext(accountEntity -> {
                    assertEquals("12345", accountEntity.getAccountNo());
                    assertEquals("Savings Account", accountEntity.getAccountType());
                    assertEquals("Active", accountEntity.getAccountStatus());
                    assertEquals("System", accountEntity.getCreatedBy());
                    assertEquals(createdDate, accountEntity.getCreatedDate());
                    assertEquals("System", accountEntity.getUpdatedBy());
                    assertEquals(updatedDate, accountEntity.getUpdatedDate());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void testSave() throws Exception {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date createdDate = simpleDateFormat.parse("2018-06-08T17:00:00");
        Date updatedDate = simpleDateFormat.parse("2019-06-08T22:00:00");

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNo("4293111199991234");
        accountEntity.setAccountType("Credit Card");
        accountEntity.setAccountStatus("Active");
        accountEntity.setCreatedBy("System");
        accountEntity.setCreatedDate(createdDate);
        accountEntity.setUpdatedBy("System");
        accountEntity.setUpdatedDate(updatedDate);

        Mono<AccountEntity> accountEntityMono = accountRepository.save(accountEntity);
        StepVerifier.create(accountEntityMono)
                .assertNext(responseAccountEntity -> {
                    assertEquals("4293111199991234", responseAccountEntity.getAccountNo());
                    assertEquals("Credit Card", responseAccountEntity.getAccountType());
                    assertEquals("Active", responseAccountEntity.getAccountStatus());
                    assertEquals("System", responseAccountEntity.getCreatedBy());
                    assertEquals(createdDate, responseAccountEntity.getCreatedDate());
                    assertEquals("System", responseAccountEntity.getUpdatedBy());
                    assertEquals(updatedDate, responseAccountEntity.getUpdatedDate());
                })
                .expectComplete()
                .verify();
    }
}
