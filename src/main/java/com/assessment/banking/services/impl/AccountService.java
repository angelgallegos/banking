package com.assessment.banking.services.impl;

import com.assessment.banking.converters.impl.account.AccountTwoWayConverter;
import com.assessment.banking.dtos.AccountDTO;
import com.assessment.banking.dtos.CustomerDTO;
import com.assessment.banking.dtos.FlatAccountDTO;
import com.assessment.banking.dtos.TransactionDTO;
import com.assessment.banking.entities.Account;
import com.assessment.banking.entities.Customer;
import com.assessment.banking.enums.AccountType;
import com.assessment.banking.enums.TransactionType;
import com.assessment.banking.exceptions.BankingNotFoundException;
import com.assessment.banking.repositories.AccountRepository;
import com.assessment.banking.services.ICRUDService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService implements ICRUDService<AccountDTO> {

    private final AccountRepository accountRepository;

    private final AccountTwoWayConverter accountTwoWayConverter;

    private final CustomerService customerService;

    public AccountDTO createFromFlatDTO(FlatAccountDTO dto) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(dto.getCustomerId());
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBalance(dto.getInitialCredit());
        accountDTO.setCustomer(customerDTO);
        accountDTO.setType(AccountType.CURRENT);
        return create(accountDTO);
    }

    @Override
    public AccountDTO create(AccountDTO dto) {
        if(dto.getBalance().compareTo(new BigDecimal(0)) > 0) {
            TransactionDTO transaction = new TransactionDTO();
            transaction.setAmount(dto.getBalance());
            transaction.setType(TransactionType.DEPOSIT);
            transaction.setAccount(dto);
            dto.setTransactions(Collections.singletonList(transaction));
        }
        Account dbo = accountRepository.save(Objects.requireNonNull(accountTwoWayConverter.toDBO(dto)));

        return accountTwoWayConverter.toDTO(dbo);
    }

    @Override
    public AccountDTO request(UUID id) {
        return accountTwoWayConverter.toDTO(get(id));
    }

    private Account get(UUID id) {
        return accountRepository.findById(id).orElseThrow(() -> new BankingNotFoundException(Account.class.getName(), id));
    }

    public void addBalance(UUID accountId, BigDecimal amount) {
        Account account = get(accountId);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    public void subBalance(UUID accountId, BigDecimal amount) {
        Account account = get(accountId);
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    @Override
    public AccountDTO update(UUID id, AccountDTO dto) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public AccountDTO delete(UUID id) {
        throw new RuntimeException("Not implemented");
    }

    public Page<AccountDTO> findAllByCustomerId(UUID id, Pageable pageable) {
        CustomerDTO customerDTO = customerService.request(id);
        return accountRepository.findAllByCustomerId(customerDTO.getId(), pageable).map(accountTwoWayConverter::toDTO);
    }
}
