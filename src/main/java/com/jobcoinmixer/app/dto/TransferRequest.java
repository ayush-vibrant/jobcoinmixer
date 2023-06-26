package com.jobcoinmixer.app.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) representing a transfer request in the Jobcoin mixing application.
 */
@Data
public class TransferRequest {
    /**
     * The address or wallet from which the Jobcoins are being transferred.
     */
    private String from;

    /**
     * The address or wallet to which the Jobcoins are being transferred.
     */
    private String to;

    /**
     * The amount of Jobcoins to be transferred.
     */
    private BigDecimal amount;
}
