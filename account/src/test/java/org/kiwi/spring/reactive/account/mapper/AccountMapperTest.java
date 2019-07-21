package org.kiwi.spring.reactive.account.mapper;

import org.kiwi.spring.reactive.account.repository.AccountEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kiwi.spring.reactive.account.web.AccountDTO;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class AccountMapperTest {

	private static final AccountMapper MAPPER = Mappers.getMapper(AccountMapper.class);

	@Before
	public void setUp() {
	}
	
	@Test
	public void testToAccountDTO() {
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setAccountNo("12345");
		accountEntity.setAccountType("Savings Account");
		accountEntity.setAccountStatus("Active");

		AccountDTO accountDTO = MAPPER.toAccountDTO(accountEntity);
		assertEquals("12345", accountDTO.getAccountNo());
		assertEquals("Savings Account", accountDTO.getAccountType());
		assertEquals("Active", accountDTO.getAccountStatus());
	}
	
	@Test
	public void testFromAccountDTO() {
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setAccountNo("1234567");
		accountDTO.setAccountType("Current Account");
		accountDTO.setAccountStatus("Dormant");

		AccountEntity accountEntity = MAPPER.fromAccountDTO(accountDTO);
		assertEquals("1234567", accountEntity.getAccountNo());
		assertEquals("Current Account", accountEntity.getAccountType());
		assertEquals("Dormant", accountEntity.getAccountStatus());
	}
}
