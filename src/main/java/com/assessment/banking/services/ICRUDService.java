package com.assessment.banking.services;

import com.assessment.banking.dtos.BaseDTO;

import java.util.UUID;

public interface ICRUDService<DTO extends BaseDTO<DTO>> {
    DTO create(DTO dto);

    DTO request(UUID id);

    DTO update(UUID id, DTO dto);

    DTO delete(UUID id);
}
