package com.assessment.banking.converters.impl.account;

import com.assessment.banking.dtos.AccountDTO;
import com.assessment.banking.dtos.CustomerDTO;
import com.assessment.banking.dtos.TransactionDTO;
import com.assessment.banking.entities.Account;
import com.assessment.banking.entities.Customer;
import com.assessment.banking.entities.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

@Component("accountDTOToDBOConverter")
@AllArgsConstructor
public class DTOToDBOConverter implements Converter<AccountDTO, Account> {

    private final Converter<CustomerDTO, Customer> customerDTOToDBOConverter;

    private final Converter<TransactionDTO, Transaction> transactionDTOToDBOConverter;

    @Override
    public Account convert(AccountDTO dto) {
        Account account = Account.builder()
            .balance(dto.getBalance())
            .type(dto.getType())
            .customer(convertCustomer(dto.getCustomer()))
            .build();

        if (!CollectionUtils.isEmpty(dto.getTransactions())) {
            List<Transaction> transactions = convertTransactions(dto.getTransactions(), account);
            account.setTransactions(transactions);
        }

        return account;
    }

    public List<Transaction> convertTransactions(Collection<TransactionDTO> transactions, Account account) {
        return transactions.stream().map(it -> this.convertTransaction(it, account)).toList();
    }

    public Transaction convertTransaction(TransactionDTO transactionDTO, Account account) {
        Transaction transaction = transactionDTOToDBOConverter.convert(transactionDTO);
        transaction.setAccount(account);

        return transaction;
    }

    public Customer convertCustomer(CustomerDTO dto) {
        return customerDTOToDBOConverter.convert(dto);
    }
}
