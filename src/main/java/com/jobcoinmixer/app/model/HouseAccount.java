package com.jobcoinmixer.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Represents the house account in the Jobcoin mixing application.
 */
@Data
@Entity
@Table(name = "house_account")
public class HouseAccount {
    @Id
    @Column(name = "house_address")
    private String houseAddress;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;
}
