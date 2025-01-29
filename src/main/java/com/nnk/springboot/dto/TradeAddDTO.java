package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TradeAddDTO {
    @NotEmpty(message = "Account is mandatory")
    private String account;

    @NotEmpty(message = "Type is mandatory")
    private String type;

    @NotEmpty(message = "Quantity is mandatory")
    private Double buyQuantity;
}
