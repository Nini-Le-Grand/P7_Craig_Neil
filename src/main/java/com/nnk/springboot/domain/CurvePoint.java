package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Represents a curve point entry in the application.
 */
@Entity
@Table(name = "curvepoint")
@Getter
@Setter
public class CurvePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer curveId;
    private Timestamp asOfDate;
    private Double term;
    private Double value;

    @Column(updatable = false)
    private Timestamp creationDate;

    @PrePersist
    protected void onCreate() {
        creationDate = new Timestamp(System.currentTimeMillis());
    }
}
