package com.assessment.banking.converters.impl.transaction;

import com.assessment.banking.converters.ITwoWayConverter;
import com.assessment.banking.dtos.TransactionDTO;
import com.assessment.banking.entities.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransactionTwoWayConverter implements ITwoWayConverter<TransactionDTO, Transaction> {

    private final Converter<TransactionDTO, Transaction> dtoToDboCustomerConverter;

    private final Converter<Transaction, TransactionDTO> dboToDtoCustomerConverter;

    @Override
    public TransactionDTO toDTO(Transaction dbo) {
        return dboToDtoCustomerConverter.convert(dbo);
    }

    @Override
    public Transaction toDBO(TransactionDTO dto) {
        return dtoToDboCustomerConverter.convert(dto);
    }
}
