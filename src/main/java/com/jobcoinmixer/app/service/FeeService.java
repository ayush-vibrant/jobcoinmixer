package com.jobcoinmixer.app.service;

import com.jobcoinmixer.app.model.Fee;
import com.jobcoinmixer.app.repository.FeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class FeeService {
    private final FeeRepository feeRepository;

    public void updateTotalFee(String depositAddress, BigDecimal fee) {
        Fee feeRecord = feeRepository.findByDepositAddress(depositAddress).orElseThrow();
        feeRecord.setTotalFee(fee);
        saveFee(feeRecord);
    }

    public void saveFee(Fee feeRecord) {
        feeRepository.save(feeRecord);
    }

    // other methods
}

