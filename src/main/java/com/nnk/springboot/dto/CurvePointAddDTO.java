package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CurvePointAddDTO {
    @NotEmpty(message = "Term is mandatory")
    private Double term;

    @NotEmpty(message = "Value is mandatory")
    private Double value;
}
