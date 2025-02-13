package com.nnk.springboot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RatingDTO {
    private Integer id;

    @NotEmpty(message = "Moodys rating is mandatory")
    private String moodysRating;

    @NotEmpty(message = "SandP rating is mandatory")
    private String sandPRating;

    @NotEmpty(message = "Fitch rating is mandatory")
    private String fitchRating;

    @NotNull(message = "Order is mandatory")
    @Min(value = 1, message = "Order must not be null")
    private Integer order;
}
