package com.assessment.banking.assemblers;

import com.assessment.banking.controllers.AccountController;
import com.assessment.banking.controllers.CustomerController;
import com.assessment.banking.dtos.AccountDTO;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AccountDTOModelAssembler extends RepresentationModelAssemblerSupport<AccountDTO, AccountDTO> {
    public AccountDTOModelAssembler() {
        super(AccountController.class, AccountDTO.class);
    }

    @Override
    public AccountDTO toModel(AccountDTO dto) {
        dto.add(linkTo(methodOn(CustomerController.class).find(dto.getId())).withSelfRel());
        return dto;
    }
}
