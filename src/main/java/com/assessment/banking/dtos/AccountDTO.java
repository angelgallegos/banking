package com.assessment.banking.dtos;

import com.assessment.banking.enums.AccountType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDTO extends BaseDTO<AccountDTO> {
    @NotNull(message = "Balance cannot be null")
    private BigDecimal balance;

    private AccountType type;

    private CustomerDTO customer;

    private List<TransactionDTO> transactions;
}
