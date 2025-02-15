package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Integer id;

    @NotBlank(message = "FullName is mandatory")
    private String fullname;

    @NotBlank(message = "Username is mandatory")
    private String username;

    private String password;

    @NotBlank(message = "Role is mandatory")
    private String role;
}
