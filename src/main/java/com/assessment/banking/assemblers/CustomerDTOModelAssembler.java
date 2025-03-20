package com.assessment.banking.assemblers;

import com.assessment.banking.controllers.CustomerController;
import com.assessment.banking.dtos.CustomerDTO;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerDTOModelAssembler extends RepresentationModelAssemblerSupport<CustomerDTO, CustomerDTO> {
    public CustomerDTOModelAssembler() {
        super(CustomerController.class, CustomerDTO.class);
    }

    @Override
    public CustomerDTO toModel(CustomerDTO dto) {
        dto.add(linkTo(methodOn(CustomerController.class).find(dto.getId())).withSelfRel());
        return dto;
    }
}
