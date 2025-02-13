package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank(message = "FullName must not be null")
    private String fullname;

    @NotBlank(message = "Username must not be null")
    private String username;

    @NotBlank(message = "Password must not be null")
    private String password;

    @NotBlank(message = "Confirm password must not be null")
    private String confirmPassword;
}
