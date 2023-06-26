package com.jobcoinmixer.app.service.utils;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Utility class to calculate fees based on the amount and number of addresses.
 */
@Service
public class FeeCalculator {

    /**
     * Calculates the fee based on the amount and number of addresses.
     *
     * @param amount            the amount for which the fee needs to be calculated
     * @param numberOfAddresses the number of withdrawal addresses
     * @return the calculated fee
     */
    public BigDecimal calculateFee(BigDecimal amount, int numberOfAddresses) {
        // Flat fee of 1% for the first address
        BigDecimal fee = amount.multiply(BigDecimal.valueOf(0.01));

        // Incremental fee of 0.15% for additional addresses
        BigDecimal additionalFee = BigDecimal.valueOf(0.0015).multiply(BigDecimal.valueOf(numberOfAddresses - 1));
        fee = fee.add(additionalFee);

        return fee;
    }
}
