package com.assessment.banking.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public abstract class BaseDTO<DTO extends RepresentationModel<? extends DTO>> extends RepresentationModel<DTO> {
    protected UUID id;

    protected Integer version = 0;
}
