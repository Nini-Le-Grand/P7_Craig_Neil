package com.nnk.springboot.dto;

import lombok.Data;

@Data
public class CurvePointDTO {
    private int id;

    private int curveId;

    private Double term;

    private Double value;
}
