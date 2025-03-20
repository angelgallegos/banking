package com.assessment.banking.assemblers;

import com.assessment.banking.controllers.TransactionController;
import com.assessment.banking.dtos.TransactionDTO;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TransactionDTOModelAssembler extends RepresentationModelAssemblerSupport<TransactionDTO, TransactionDTO> {
    public TransactionDTOModelAssembler() {
        super(TransactionController.class, TransactionDTO.class);
    }

    @Override
    public TransactionDTO toModel(TransactionDTO dto) {
        dto.add(linkTo(methodOn(TransactionController.class).find(dto.getId())).withSelfRel());
        return dto;
    }
}
