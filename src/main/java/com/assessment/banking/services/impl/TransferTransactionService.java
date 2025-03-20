package com.assessment.banking.services.impl;

import com.assessment.banking.converters.impl.transaction.TransactionTwoWayConverter;
import com.assessment.banking.dtos.AccountDTO;
import com.assessment.banking.dtos.TransactionDTO;
import com.assessment.banking.entities.Transaction;
import com.assessment.banking.exceptions.InvalidOperationException;
import com.assessment.banking.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferTransactionService extends TransactionService {

    public TransferTransactionService(
        TransactionRepository transactionRepository,
        TransactionTwoWayConverter transactionTwoWayConverter,
        AccountService accountService
    ) {
        super(transactionRepository, transactionTwoWayConverter, accountService);
    }

    @Override
    @Transactional
    public TransactionDTO create(TransactionDTO dto) {
        Transaction transaction = transactionRepository.save(transactionTwoWayConverter.toDBO(dto));
        operation(dto);
        return transactionTwoWayConverter.toDTO(transaction);
    }

    @Override
    public void operation(TransactionDTO transactionDTO) {
        validate(transactionDTO);
        accountService.subBalance(transactionDTO.getAccount().getId(), transactionDTO.getAmount());
    }

    private void validate(TransactionDTO transactionDTO) {
        AccountDTO accountDTO = accountService.request(transactionDTO.getAccount().getId());
        if (accountDTO.getBalance().subtract(transactionDTO.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOperationException("not enough funds");
        }
    }
}
