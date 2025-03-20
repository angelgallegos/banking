package com.assessment.banking.converters.impl.account;

import com.assessment.banking.converters.ITwoWayConverter;
import com.assessment.banking.dtos.AccountDTO;
import com.assessment.banking.entities.Account;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AccountTwoWayConverter implements ITwoWayConverter<AccountDTO, Account> {

    private final Converter<AccountDTO, Account> dtoToDboAccountConverter;

    private final Converter<Account, AccountDTO> dboToDtoAccountConverter;

    @Override
    public AccountDTO toDTO(Account dbo) {
        return dboToDtoAccountConverter.convert(dbo);
    }

    @Override
    public Account toDBO(AccountDTO dto) {
        return dtoToDboAccountConverter.convert(dto);
    }
}
