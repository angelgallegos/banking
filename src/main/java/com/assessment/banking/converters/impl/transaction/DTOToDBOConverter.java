package com.assessment.banking.converters.impl.transaction;

import com.assessment.banking.dtos.AccountDTO;
import com.assessment.banking.dtos.TransactionDTO;
import com.assessment.banking.entities.Account;
import com.assessment.banking.entities.Transaction;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component("transactionDTOToDBOConverter")
public class DTOToDBOConverter implements Converter<TransactionDTO, Transaction> {

    @Override
    public Transaction convert(TransactionDTO dto) {
        return Transaction.builder()
                .type(dto.getType())
                .amount(dto.getAmount())
                .account(convertAccount(dto.getAccount()))
                .build();
    }

    private Account convertAccount(AccountDTO dto) {
        return Account.builder()
                .id(dto.getId())
                .balance(dto.getBalance())
                .type(dto.getType())
                .version(dto.getVersion())
                .build();
    }
}
