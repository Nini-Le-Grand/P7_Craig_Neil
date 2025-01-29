package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RuleNameUpdateDTO {
    private int id;

    @NotEmpty(message = "Name must not be null")
    private String name;

    @NotEmpty(message = "Description must not be null")
    private String description;

    @NotEmpty(message = "JSON must not be null")
    private String json;

    @NotEmpty(message = "Template must not be null")
    private String template;

    @NotEmpty(message = "SQL must not be null")
    private String sql;

    @NotEmpty(message = "SQLPart must not be null")
    private String sqlPart;
}
