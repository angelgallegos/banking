package com.assessment.banking.converters.impl.transaction;

import com.assessment.banking.dtos.TransactionDTO;
import com.assessment.banking.entities.Transaction;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component("transactionDBOToDTOConverter")
public class DBOToDTOConverter implements Converter<Transaction, TransactionDTO> {
    @Override
    public TransactionDTO convert(Transaction dbo) {
        TransactionDTO dto = new TransactionDTO();
        dto.setAmount(dbo.getAmount());
        dto.setType(dbo.getType());
        dto.setId(dbo.getId());

        return dto;
    }
}
