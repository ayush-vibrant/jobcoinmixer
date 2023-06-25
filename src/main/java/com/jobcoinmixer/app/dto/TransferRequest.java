package com.jobcoinmixer.app.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    private String from;
    private String to;
    private BigDecimal amount;
}

