package com.jobcoinmixer.app.service.utils;

import java.math.BigDecimal;

public class FeeCalculator {
    public BigDecimal calculateFee(BigDecimal amount, int numberOfAddresses) {
        // Flat fee of 1% for the first address
        BigDecimal fee = amount.multiply(BigDecimal.valueOf(0.01));

        // Incremental fee of 0.15% for additional addresses
        BigDecimal additionalFee = BigDecimal.valueOf(0.0015).multiply(BigDecimal.valueOf(numberOfAddresses - 1));
        fee = fee.add(additionalFee);

        return fee;
    }
}
