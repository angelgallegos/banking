package com.assessment.banking.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Customer extends BaseDBO {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Surname is mandatory")
    private String surname;

    @OneToMany(mappedBy="customer", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Account> accounts = new HashSet<>();

    @Formula("(SELECT SUM(A.balance) FROM customer C JOIN account A ON C.id = A.customer_id WHERE C.id=id)")
    private BigDecimal balance;
}
