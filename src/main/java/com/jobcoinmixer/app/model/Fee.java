package com.jobcoinmixer.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents a fee collection in the Jobcoin mixing application.
 */
@Data
@Entity
@Table(name = "fee_collection")
@AllArgsConstructor
@NoArgsConstructor
public class Fee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "deposit_address")
    private String depositAddress;

    @Column(name = "total_fee")
    private BigDecimal totalFee;

    public Fee(String depositAddress, BigDecimal totalFee) {
        this.depositAddress = depositAddress;
        this.totalFee = totalFee;
    }
}
