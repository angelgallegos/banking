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

@Component("accountDBOToDTOConverter")
@AllArgsConstructor
public class DBOToDTOConverter implements Converter<Account, AccountDTO> {

    private final Converter<Customer, CustomerDTO> customerCustomerDTOConverter;
    private final Converter<Transaction, TransactionDTO> transactionDTOConverter;

    @Override
    public AccountDTO convert(Account dbo) {
        AccountDTO dto = new AccountDTO();
        dto.setBalance(dbo.getBalance());
        dto.setType(dbo.getType());
        dto.setCustomer(getCustomer(dbo.getCustomer()));
        dto.setId(dbo.getId());
        if(!CollectionUtils.isEmpty(dbo.getTransactions())) {
            dto.setTransactions(convertTransactions(dbo.getTransactions()));
        }
        return dto;
    }

    public CustomerDTO getCustomer(Customer dbo) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(dbo.getId());
        dto.setName(dbo.getName());
        dto.setSurname(dbo.getSurname());
        dto.setBalance(dbo.getBalance());

        return dto;
    }

    public List<TransactionDTO> convertTransactions(Collection<Transaction> transactions) {
        return transactions.stream().map(transactionDTOConverter::convert).toList();
    }
}
