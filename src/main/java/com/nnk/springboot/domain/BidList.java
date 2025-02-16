package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Represents a bid list entry in the application.
 */
@Entity
@Table(name = "bidlist")
@Getter
@Setter
public class BidList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String account;
    private String type;
    private Double bidQuantity;
    private Double askQuantity;
    private Double bid;
    private Double ask;
    private String benchmark;
    private Timestamp bidListDate;
    private String commentary;
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


