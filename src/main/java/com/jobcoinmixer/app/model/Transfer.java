package com.jobcoinmixer.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Represents a transfer in the Jobcoin mixing application.
 */
@Data
@Entity
@Table(name = "transfer_table")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "withdrawal_wallet_address")
    private String withdrawalWalletAddress;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "status")
    private String status;

    @Column(name = "deposit_address")
    private String depositAddress;
}
