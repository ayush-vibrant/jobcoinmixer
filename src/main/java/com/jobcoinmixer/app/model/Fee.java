package com.jobcoinmixer.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Represents a fee collection in the Jobcoin mixing application.
 */
@Data
@Entity
@Table(name = "fee_collection")
public class Fee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "deposit_address")
    private String depositAddress;

    @Column(name = "total_fee")
    private BigDecimal totalFee;
}
