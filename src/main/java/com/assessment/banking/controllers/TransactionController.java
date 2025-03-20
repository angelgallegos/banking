package com.assessment.banking.controllers;

import com.assessment.banking.assemblers.TransactionDTOModelAssembler;
import com.assessment.banking.dtos.TransactionDTO;
import com.assessment.banking.enums.TransactionType;
import com.assessment.banking.services.impl.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    private final TransactionDTOModelAssembler transactionDTOModelAssembler;

    private final PagedResourcesAssembler<TransactionDTO> pagedResourcesAssembler;

    private final BeanFactory beanFactory;

    @PostMapping
    TransactionDTO add(@RequestBody @Validated TransactionDTO dto) {
        return beanFactory.getBean(Objects.requireNonNull(TransactionType.findByType(dto.getType().getValue())).getImplementation()).create(dto);
    }

    @GetMapping("/{id}")
    public TransactionDTO find(@PathVariable UUID id) {
        TransactionDTO transactionDTO = transactionService.request(id);
        TransactionDTO model = transactionDTOModelAssembler.toModel(transactionDTO);
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).all(Pageable.ofSize(10))).withRel("list"));

        return model;
    }

    @GetMapping("/list/{accountId}")
    public PagedModel<TransactionDTO> allByAccountId(@PathVariable UUID accountId, Pageable pageable) {

        return pagedResourcesAssembler.toModel(transactionService.findAllByAccountId(accountId, pageable), transactionDTOModelAssembler);
    }
}
