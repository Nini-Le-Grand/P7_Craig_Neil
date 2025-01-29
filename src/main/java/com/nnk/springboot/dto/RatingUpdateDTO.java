package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RatingUpdateDTO {
    private int id;

    @NotEmpty(message = "Moodys rating must not be null")
    private String moodysRating;

    @NotEmpty(message = "SandP rating must not be null")
    private String sandPRating;

    @NotEmpty(message = "Fitch rating must not be null")
    private String fitchRating;

    @NotEmpty(message = "Order must not be null")
    private int orderNumber;
}
