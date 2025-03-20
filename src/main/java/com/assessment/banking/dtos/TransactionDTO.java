package com.assessment.banking.dtos;

import com.assessment.banking.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO extends BaseDTO<TransactionDTO> {
    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=38, fraction=2)
    private BigDecimal amount;

    private AccountDTO account;

    private TransactionType type;
}
