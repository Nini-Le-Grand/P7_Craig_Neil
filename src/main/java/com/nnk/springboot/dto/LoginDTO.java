package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for user login credentials.
 */
@Getter
@Setter
public class LoginDTO {
    @NotBlank(message = "Username must not be null")
    private String username;

    @NotBlank(message = "Password must not be null")
    private String password;
}
