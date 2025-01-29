package com.nnk.springboot.dto;

import lombok.Data;

@Data
public class RuleNameDTO {
    private int id;

    private String name;

    private String description;

    private String json;

    private String template;

    private String sql;

    private String sqlPart;
}
