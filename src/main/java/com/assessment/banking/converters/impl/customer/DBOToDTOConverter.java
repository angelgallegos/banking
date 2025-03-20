package com.assessment.banking.converters.impl.customer;

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

@Component("customerDBOToDTOConverter")
@AllArgsConstructor
public class DBOToDTOConverter implements Converter<Customer, CustomerDTO> {

    public final Converter<Transaction, TransactionDTO> transactionDTOConverter;

    @Override
    public CustomerDTO convert(Customer dbo) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(dbo.getId());
        dto.setName(dbo.getName());
        dto.setSurname(dbo.getSurname());
        dto.setBalance(dbo.getBalance());
        if (!CollectionUtils.isEmpty(dbo.getAccounts())) {
            dto.setAccounts(convertAccounts(dbo.getAccounts()));
        }

        return dto;
    }

    public List<AccountDTO> convertAccounts(Collection<Account> accounts) {
        return accounts.stream().map(this::convertAccount).toList();
    }

    public AccountDTO convertAccount(Account account) {
        AccountDTO dto =  new AccountDTO();
        dto.setId(account.getId());
        dto.setBalance(account.getBalance());
        dto.setType(account.getType());
        if (!CollectionUtils.isEmpty(account.getTransactions())) {
            dto.setTransactions(convertTransactions(account.getTransactions()));
        }

        return dto;
    }

    public List<TransactionDTO> convertTransactions(Collection<Transaction> transactions) {
        return transactions.stream().map(transactionDTOConverter::convert).toList();
    }
}
