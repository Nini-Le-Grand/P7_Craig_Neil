package com.nnk.springboot.dto;

import lombok.Data;

@Data
public class BidListDto {
    private int id;

    private String account;

    private String type;

    private Double bidQuantity;
}

