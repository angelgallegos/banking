package com.assessment.banking.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class FlatAccountDTO {
    @JsonProperty("customerID")
    @NotNull(message = "Customer id must not be null")
    private UUID customerId;
    @NotNull(message = "Initial credit must not be null")
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=38, fraction=2)
    private BigDecimal initialCredit;

}
