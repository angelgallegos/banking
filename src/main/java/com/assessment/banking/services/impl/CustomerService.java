package com.assessment.banking.services.impl;

import com.assessment.banking.converters.impl.customer.CustomerTwoWayConverter;
import com.assessment.banking.dtos.CustomerDTO;
import com.assessment.banking.entities.Customer;
import com.assessment.banking.exceptions.BankingNotFoundException;
import com.assessment.banking.exceptions.InvalidOperationException;
import com.assessment.banking.repositories.CustomerRepository;
import com.assessment.banking.services.ICRUDService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerService implements ICRUDService<CustomerDTO> {

    private final CustomerRepository customerRepository;

    private final CustomerTwoWayConverter twoWayConverter;

    @Override
    public CustomerDTO create(CustomerDTO dto) {
        Customer dbo = customerRepository.save(Objects.requireNonNull(twoWayConverter.toDBO(dto)));

        return twoWayConverter.toDTO(dbo);
    }

    @Override
    public CustomerDTO request(UUID id) {
        return twoWayConverter.toDTO(customerRepository.findById(id).orElseThrow(() -> new BankingNotFoundException(Customer.class.getName(), id)));
    }

    @Override
    public CustomerDTO update(UUID id, CustomerDTO dto) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public CustomerDTO delete(UUID id) {
        throw new InvalidOperationException("cannot update a transaction");
    }

    public Page<CustomerDTO> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable).map(twoWayConverter::toDTO);
    }
}
