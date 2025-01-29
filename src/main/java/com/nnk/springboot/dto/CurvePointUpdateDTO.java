package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CurvePointUpdateDTO {
    private int id;

    @NotEmpty(message = "Term must not be null")
    private Double term;

    @NotEmpty(message = "Value must not be null")
    private Double value;
}
