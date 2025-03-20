package com.assessment.banking.controllers;

import com.assessment.banking.dtos.CustomerDTO;
import com.assessment.banking.entities.Customer;
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
@TestPropertySource(locations = "classpath:application-test.properties")
public class CustomerControllerITests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        customerRepository.deleteAll();
    }

    @Test
    public void givenCustomerObject_whenCreateCustomer_thenReturnSavedCustomer() throws Exception {
        CustomerDTO customer = new CustomerDTO();
                customer.setName("Angel");
                customer.setSurname("Gallegos");

        ResultActions response = mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)));

        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.name",
                        is(customer.getName())))
                .andExpect(jsonPath("$.surname",
                        is(customer.getSurname())));

    }

    @Test
    public void givenCustomerId_whenGetCustomerById_thenReturnCustomerObject() throws Exception {
        Customer customer = customerRepository.save(Customer.builder().name("Angel").surname("Gallegos").build());

        ResultActions response = mockMvc.perform(get("/customer/{id}", customer.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(customer.getName())))
                .andExpect(jsonPath("$.surname", is(customer.getSurname())));
    }

    @Test
    public void givenListOfCustomers_whenGetAllCustomers_thenReturnCustomersList() throws Exception {
        List<Customer> listOfCustomers = new ArrayList<>();
        listOfCustomers.add(Customer.builder().name("Angel").surname("Gallegos").build());
        listOfCustomers.add(Customer.builder().name("Tony").surname("Stark").build());
        customerRepository.saveAll(listOfCustomers);
        ResultActions response = mockMvc.perform(get("/customer/list"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.customerDTOList.size()",
                        is(listOfCustomers.size())));

    }
}
