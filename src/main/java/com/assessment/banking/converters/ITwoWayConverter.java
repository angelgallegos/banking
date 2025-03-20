package com.assessment.banking.converters;

import com.assessment.banking.dtos.BaseDTO;
import com.assessment.banking.entities.BaseDBO;
import org.springframework.hateoas.RepresentationModel;

public interface ITwoWayConverter<DTO extends BaseDTO<? extends RepresentationModel<?>>, DBO extends BaseDBO> {
    DTO toDTO(DBO dbo);
    DBO toDBO(DTO dto);
}
