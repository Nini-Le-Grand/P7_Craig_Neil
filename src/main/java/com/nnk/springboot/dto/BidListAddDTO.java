package com.nnk.springboot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BidListAddDTO {
    @NotBlank(message = "Account is mandatory")
    private String account;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @Min(value = 1, message = "Quantity must not be null")
    private int bidQuantity;
}
