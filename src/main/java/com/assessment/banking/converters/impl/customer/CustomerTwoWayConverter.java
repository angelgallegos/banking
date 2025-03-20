package com.assessment.banking.converters.impl.customer;

import com.assessment.banking.converters.ITwoWayConverter;
import com.assessment.banking.dtos.CustomerDTO;
import com.assessment.banking.entities.Customer;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CustomerTwoWayConverter implements ITwoWayConverter<CustomerDTO, Customer> {

    private final Converter<CustomerDTO, Customer> dtoToDboCustomerConverter;
    private final Converter<Customer, CustomerDTO> dboToDtoCustomerConverter;

    @Override
    public CustomerDTO toDTO(Customer dbo) {
        return dboToDtoCustomerConverter.convert(dbo);
    }

    @Override
    public Customer toDBO(CustomerDTO dto) {
        return dtoToDboCustomerConverter.convert(dto);
    }
}
