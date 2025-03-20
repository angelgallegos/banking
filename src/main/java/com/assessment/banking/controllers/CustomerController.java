package com.assessment.banking.controllers;

import com.assessment.banking.assemblers.CustomerDTOModelAssembler;
import com.assessment.banking.dtos.CustomerDTO;
import com.assessment.banking.services.impl.CustomerService;
import lombok.RequiredArgsConstructor;
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

import java.util.UUID;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    private final CustomerDTOModelAssembler customerDTOModelAssembler;

    private final PagedResourcesAssembler<CustomerDTO> pagedResourcesAssembler;

    @PostMapping
    public CustomerDTO add(@RequestBody @Validated CustomerDTO dto) {
        return customerService.create(dto);
    }

    @GetMapping("/{id}")
    public CustomerDTO find(@PathVariable UUID id) {
        CustomerDTO customerDTO = customerService.request(id);
        CustomerDTO model = customerDTOModelAssembler.toModel(customerDTO);
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).all(Pageable.ofSize(10))).withRel("list"));

        return model;
    }

    @GetMapping("/list")
    public PagedModel<CustomerDTO> all(Pageable pageable) {

        return pagedResourcesAssembler.toModel(customerService.findAll(pageable), customerDTOModelAssembler);
    }
}
