package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TradeUpdateDTO {
    private int tradeId;

    @NotEmpty(message = "Account must not be null")
    private String account;

    @NotEmpty(message = "Type must not be null")
    private String type;

    @NotEmpty(message = "Quantity must not be null")
    private Double buyQuantity;
}
