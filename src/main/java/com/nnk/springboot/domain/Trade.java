package com.nnk.springboot.domain;


import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Represents a trade entry in the application.
 */
@Entity
@Table(name = "trade")
@Getter
@Setter
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String account;
    private String type;
    private Double buyQuantity;
    private Double sellQuantity;
    private Double buyPrice;
    private Double sellPrice;
    private String benchmark;
    private Timestamp tradeDate;
    private String security;
    private String status;
    private String trader;
    private String book;
    private String creationName;

    @Column(updatable = false)
    private Timestamp creationDate;

    private String revisionName;
    private Timestamp revisionDate;
    private String dealName;
    private String dealType;
    private String sourceListId;
    private String side;

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        creationDate = now;
        revisionDate = now;
    }

    @PreUpdate
    protected void onUpdate() {
        revisionDate = new Timestamp(System.currentTimeMillis());
    }
}
