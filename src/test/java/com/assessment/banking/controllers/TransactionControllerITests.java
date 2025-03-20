package com.assessment.banking.controllers;

import com.assessment.banking.dtos.AccountDTO;
import com.assessment.banking.dtos.TransactionDTO;
import com.assessment.banking.entities.Account;
import com.assessment.banking.entities.Customer;
import com.assessment.banking.entities.Transaction;
import com.assessment.banking.enums.TransactionType;
import com.assessment.banking.repositories.AccountRepository;
import com.assessment.banking.repositories.CustomerRepository;
import com.assessment.banking.repositories.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class TransactionControllerITests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void givenTransactionObject_whenCreateTransaction_thenReturnSavedTransaction() throws Exception {
        Customer customer = customerRepository.save(getCustomer());
        Account account = getAccount(customer, new BigDecimal(60));
        accountRepository.save(account);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccount(buildAccountDTO(account));
        transactionDTO.setAmount(new BigDecimal(10));
        transactionDTO.setType(TransactionType.TRANSFER);

        ResultActions response = mockMvc.perform(post("/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)));

        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.amount",
                        is(10)))
                .andExpect(jsonPath("$.type",
                        is(TransactionType.TRANSFER.name())));
    }

    @Test
    public void givenTransactionId_whenGetTransactionById_thenReturnTransactionObject() throws Exception {
        Customer customer = customerRepository.save(getCustomer());
        Account account = getAccount(customer, new BigDecimal(60));
        accountRepository.save(account);
        Transaction transaction = transactionRepository.save(
            Transaction.builder().account(account).amount(new BigDecimal(10)).type(TransactionType.DEPOSIT).build()
        );

        ResultActions response = mockMvc.perform(get("/transaction/{id}", transaction.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(transaction.getId().toString())))
                .andExpect(jsonPath("$.amount", is(10.0)))
                .andExpect(jsonPath("$.type", is(TransactionType.DEPOSIT.name())));
    }

    @Test
    public void givenListOfTransactions_whenGetAllTransactionsByAccountId_thenReturnTransactionsList() throws Exception {
        Customer customer = customerRepository.save(getCustomer());
        Account account = getAccount(customer, new BigDecimal(60));
        accountRepository.save(account);

        List<Transaction> listOfTransactions = new ArrayList<>();
        listOfTransactions.add(Transaction.builder().account(account).amount(new BigDecimal(100)).type(TransactionType.DEPOSIT).build());
        listOfTransactions.add(Transaction.builder().account(account).amount(new BigDecimal(30)).type(TransactionType.TRANSFER).build());
        listOfTransactions.add(Transaction.builder().account(account).amount(new BigDecimal(20)).type(TransactionType.TRANSFER).build());
        transactionRepository.saveAll(listOfTransactions);
        ResultActions response = mockMvc.perform(get("/transaction/list/{id}", account.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.transactionDTOList.size()",
                        is(listOfTransactions.size())));

    }

    private AccountDTO buildAccountDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());

        return accountDTO;
    }

    private Customer getCustomer() {
        return Customer.builder().name("Angel").surname("Gallegos").build();
    }

    private Account getAccount(Customer customer, BigDecimal amount) {
        return Account.builder()
                .customer(customer)
                .balance(amount)
                .build();
    }
}
