package com.nnk.springboot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidListDTO {
    private Integer id;

    @NotEmpty(message = "Account is mandatory")
    private String account;

    @NotEmpty(message = "Type is mandatory")
    private String type;

    @NotNull(message = "Quantity is mandatory")
    @Min(value = 1, message = "Quantity must not be null")
    private Double bidQuantity;
}


