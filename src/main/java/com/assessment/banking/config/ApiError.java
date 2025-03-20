package com.assessment.banking.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private String message;
    private List<String> errors;
    private HttpStatus httpStatus;
}
