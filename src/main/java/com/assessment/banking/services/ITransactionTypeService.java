package com.assessment.banking.services;

import com.assessment.banking.dtos.TransactionDTO;

public interface ITransactionTypeService {
    TransactionDTO create(TransactionDTO dto);
    void operation(TransactionDTO transactionDTO);
}
