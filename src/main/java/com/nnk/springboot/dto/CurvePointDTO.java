package com.nnk.springboot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for curve point entries.
 */
@Getter
@Setter
public class CurvePointDTO {
    private Integer id;

    private Integer curveId;

    @NotNull(message = "Term is mandatory")
    @Min(value = 1, message = "Term must not be null")
    private Double term;

    @NotNull(message = "Value is mandatory")
    @Min(value = 1, message = "Value must not be null")
    private Double value;
}
