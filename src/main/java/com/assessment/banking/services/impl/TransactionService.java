package com.assessment.banking.services.impl;

import com.assessment.banking.converters.impl.transaction.TransactionTwoWayConverter;
import com.assessment.banking.dtos.AccountDTO;
import com.assessment.banking.dtos.TransactionDTO;
import com.assessment.banking.entities.Transaction;
import com.assessment.banking.exceptions.BankingNotFoundException;
import com.assessment.banking.exceptions.InvalidOperationException;
import com.assessment.banking.repositories.TransactionRepository;
import com.assessment.banking.services.ICRUDService;
import com.assessment.banking.services.ITransactionTypeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class TransactionService implements ICRUDService<TransactionDTO>, ITransactionTypeService {

    protected final TransactionRepository transactionRepository;

    protected final TransactionTwoWayConverter transactionTwoWayConverter;

    protected final AccountService accountService;

    @Override
    public TransactionDTO create(TransactionDTO dto) {
        Transaction transaction = transactionRepository.save(transactionTwoWayConverter.toDBO(dto));
        return transactionTwoWayConverter.toDTO(transaction);
    }

    @Override
    public TransactionDTO request(UUID id) {
        return transactionTwoWayConverter.toDTO(transactionRepository.findById(id).orElseThrow(() -> new BankingNotFoundException(Transaction.class.getName(), id)));
    }

    @Override
    public TransactionDTO update(UUID id, TransactionDTO dto) {
        throw new InvalidOperationException("cannot update a transaction");
    }

    @Override
    public TransactionDTO delete(UUID id) {
        throw new RuntimeException("Not implemented");
    }

    public Page<TransactionDTO> findAllByAccountId(UUID accountId, Pageable pageable) {
        AccountDTO accountDTO = accountService.request(accountId);
        return transactionRepository.findAllByAccountId(accountId, pageable).map(transactionTwoWayConverter::toDTO);
    }

    @Override
    public void operation(TransactionDTO transactionDTO) {
        throw new RuntimeException("Not implemented");
    }
}
