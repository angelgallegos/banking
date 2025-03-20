package com.assessment.banking.controllers;

import com.assessment.banking.BankingApplication;
import com.assessment.banking.dtos.FlatAccountDTO;
import com.assessment.banking.entities.Account;
import com.assessment.banking.entities.Customer;
import com.assessment.banking.repositories.AccountRepository;
import com.assessment.banking.repositories.CustomerRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BankingApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class AccountControllerITests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void givenAccountObject_whenCreateAccount_thenReturnSavedAccount() throws Exception {
        Customer customer = customerRepository.save(getCustomer());
        FlatAccountDTO flatAccountDTO = FlatAccountDTO.builder()
                .customerId(customer.getId())
                .initialCredit(new BigDecimal("10.60"))
                .build();

        ResultActions response = mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flatAccountDTO)));

        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.balance",
                        is(10.60)));
    }

    @Test
    public void givenAccountId_whenGetAccountById_thenReturnAccountObject() throws Exception {
        Customer customer = customerRepository.save(getCustomer());
        Account account = getAccount(customer, new BigDecimal(60));
        accountRepository.save(account);

        ResultActions response = mockMvc.perform(get("/account/{id}", account.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.customer.id", is(customer.getId().toString())))
                .andExpect(jsonPath("$.balance", is(60.0)));
    }

    @Test
    public void givenListOfAccounts_whenGetAllAccounts_thenReturnAccountsList() throws Exception {
        Customer customer = customerRepository.save(getCustomer());
        List<Account> listOfAccounts = new ArrayList<>();
        listOfAccounts.add(getAccount(customer, new BigDecimal(60)));
        listOfAccounts.add(getAccount(customer, new BigDecimal(40)));
        accountRepository.saveAll(listOfAccounts);
        ResultActions response = mockMvc.perform(get("/account/list/{id}", customer.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.accountDTOList.size()",
                        is(listOfAccounts.size())))
                .andExpect(jsonPath("$._embedded.accountDTOList[0].balance",
                        is(60.0)));

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
