package com.reliaquest.api.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeDTO {

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotNull(message = "Salary is required") @Min(value = 1, message = "Salary must be greater than zero")
    private Integer salary;

    @NotNull(message = "Age is required") @Min(value = 16, message = "Minimum allowed age is 16")
    @Max(value = 75, message = "Maximum allowed age is 75")
    private Integer age;

    @NotBlank(message = "Title must not be blank")
    private String title;
}
