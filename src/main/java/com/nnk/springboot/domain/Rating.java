package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a rating entry in the application.
 */
@Entity
@Table(name = "rating")
@Getter
@Setter
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String moodysRating;
    private String sandPRating;
    private String fitchRating;
    private Integer orderNumber;
}
