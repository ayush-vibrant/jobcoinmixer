package com.jobcoinmixer.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents the details of a withdrawal in the Jobcoin mixing application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalDetail {
    private String withdrawalAddress;
    private BigDecimal amount;
    private String status;
}
