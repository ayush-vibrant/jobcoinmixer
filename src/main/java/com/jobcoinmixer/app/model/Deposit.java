package com.jobcoinmixer.app.model;

import com.jobcoinmixer.app.dto.DepositStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a deposit in the Jobcoin mixing application.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deposit_table")
public class Deposit {
    @Id
    @Column(name = "deposit_address")
    private String depositAddress;

    @Column(name = "withdrawal_addresses")
    private List<String> withdrawalAddresses;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DepositStatus status;
}
