package com.jobcoinmixer.app.service;

import com.jobcoinmixer.app.model.Fee;
import com.jobcoinmixer.app.repository.FeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service class for managing fees.
 */
@Service
@AllArgsConstructor
public class FeeService {
    private final FeeRepository feeRepository;

    /**
     * Updates the total fee for a specific deposit address.
     *
     * @param depositAddress the deposit address
     * @param fee            the total fee to update
     * @throws java.util.NoSuchElementException if the fee record for the deposit address is not found
     */
    public void updateTotalFee(String depositAddress, BigDecimal fee) {
        Fee feeRecord = feeRepository.findByDepositAddress(depositAddress).orElseThrow();
        feeRecord.setTotalFee(fee);
        saveFee(feeRecord);
    }

    /**
     * Saves a fee record.
     *
     * @param feeRecord the fee record to save
     */
    public void saveFee(Fee feeRecord) {
        feeRepository.save(feeRecord);
    }

}
