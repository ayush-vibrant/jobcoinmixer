package com.jobcoinmixer.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalDetail {
    private String withdrawalAddress;
    private BigDecimal amount;
    private String status;

}

