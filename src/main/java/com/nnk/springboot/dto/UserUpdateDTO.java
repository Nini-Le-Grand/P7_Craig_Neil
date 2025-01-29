package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserUpdateDTO {
    private int id;

    @NotEmpty(message = "Fullname must not be null")
    private String fullname;

    @NotEmpty(message = "Username must not be null")
    private String username;

    @NotEmpty(message = "Password must not be null")
    private String password;

    @NotEmpty(message = "Role must not be null")
    private String role;
}
