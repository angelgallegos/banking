package com.assessment.banking.controllers;

import com.assessment.banking.assemblers.AccountDTOModelAssembler;
import com.assessment.banking.dtos.AccountDTO;
import com.assessment.banking.dtos.FlatAccountDTO;
import com.assessment.banking.services.impl.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    private final AccountDTOModelAssembler accountDTOModelAssembler;

    private final PagedResourcesAssembler<AccountDTO> pagedResourcesAssembler;

    @PostMapping
    public AccountDTO add(@RequestBody @Validated FlatAccountDTO dto) {
        return accountService.createFromFlatDTO(dto);
    }

    @GetMapping("/{id}")
    public AccountDTO request(@PathVariable UUID id) {
        return accountService.request(id);
    }

    @GetMapping("/list/{customerId}")
    public PagedModel<AccountDTO> all(@PathVariable UUID customerId, Pageable pageable) {

        return pagedResourcesAssembler.toModel(accountService.findAllByCustomerId(customerId, pageable), accountDTOModelAssembler);
    }
}
