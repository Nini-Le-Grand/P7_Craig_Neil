package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RuleNameAddDTO {
    @NotEmpty(message = "Name is mandatory")
    private String name;

    @NotEmpty(message = "Description is mandatory")
    private String description;

    @NotEmpty(message = "JSON is mandatory")
    private String json;

    @NotEmpty(message = "Template is mandatory")
    private String template;

    @NotEmpty(message = "SQL is mandatory")
    private String sql;

    @NotEmpty(message = "SQLPart is mandatory")
    private String sqlPart;
}
