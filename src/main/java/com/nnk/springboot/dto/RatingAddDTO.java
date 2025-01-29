package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RatingAddDTO {
    @NotEmpty(message = "Moodys rating is mandatory")
    private String moodysRating;

    @NotEmpty(message = "SandP rating is mandatory")
    private String sandPRating;

    @NotEmpty(message = "Fitch rating is mandatory")
    private String fitchRating;

    @NotEmpty(message = "Order must not be null")
    private int orderNumber;
}
