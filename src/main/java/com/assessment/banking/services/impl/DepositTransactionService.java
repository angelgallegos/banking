package com.assessment.banking.services.impl;

import com.assessment.banking.converters.impl.transaction.TransactionTwoWayConverter;
import com.assessment.banking.dtos.TransactionDTO;
import com.assessment.banking.entities.Transaction;
import com.assessment.banking.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DepositTransactionService extends TransactionService {

    public DepositTransactionService(
        TransactionRepository transactionRepository,
        TransactionTwoWayConverter transactionTwoWayConverter,
        AccountService accountService
    ) {
        super(transactionRepository, transactionTwoWayConverter, accountService);
    }

    @Override
    public void operation(TransactionDTO transactionDTO) {
        accountService.addBalance(transactionDTO.getAccount().getId(), transactionDTO.getAmount());
    }

    @Override
    @Transactional
    public TransactionDTO create(TransactionDTO dto) {
        Transaction transaction = transactionRepository.save(transactionTwoWayConverter.toDBO(dto));
        operation(dto);
        return transactionTwoWayConverter.toDTO(transaction);
    }
}
