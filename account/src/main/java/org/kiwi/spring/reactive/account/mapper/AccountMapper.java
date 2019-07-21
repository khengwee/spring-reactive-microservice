package org.kiwi.spring.reactive.account.mapper;

import org.kiwi.spring.reactive.account.repository.AccountEntity;
import org.kiwi.spring.reactive.account.web.AccountDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountMapper {

    @Mapping(source = "accountNo", target = "accountNo")
    @Mapping(source = "accountType", target = "accountType")
    @Mapping(source = "accountStatus", target = "accountStatus")
    AccountDTO toAccountDTO(AccountEntity accountEntity);

    @InheritInverseConfiguration
    AccountEntity fromAccountDTO(AccountDTO accountDTO);
}
