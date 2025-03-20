package com.assessment.banking.converters.impl.customer;

import com.assessment.banking.dtos.AccountDTO;
import com.assessment.banking.dtos.CustomerDTO;
import com.assessment.banking.entities.Account;
import com.assessment.banking.entities.Customer;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component("customerDTOToDBOConverter")
@AllArgsConstructor
public class DTOToDBOConverter implements Converter<CustomerDTO, Customer> {

    @Override
    public Customer convert(CustomerDTO dto) {
        Customer customer = Customer.builder()
                .id(dto.getId())
                .name(dto.getName())
                .surname(dto.getSurname())
                .version(dto.getVersion())
                .build();

        if (!CollectionUtils.isEmpty(dto.getAccounts())) {
            Set<Account> accounts = convertAccounts(dto.getAccounts(), customer);
            customer.setAccounts(accounts);
        }

        return customer;
    }

    private Set<Account> convertAccounts(Collection<AccountDTO> accounts, Customer customer) {
        return accounts.stream().map(it -> this.convertAccount(it, customer)).collect(Collectors.toSet());
    }

    private Account convertAccount(AccountDTO accountDTO, Customer customer) {
        return Account.builder()
            .balance(accountDTO.getBalance())
            .type(accountDTO.getType())
            .customer(customer)
            .build();
    }
}
