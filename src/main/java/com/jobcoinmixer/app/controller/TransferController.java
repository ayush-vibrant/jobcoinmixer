package com.jobcoinmixer.app.controller;

import com.jobcoinmixer.app.Paths;
import com.jobcoinmixer.app.dto.ApiResponse;
import com.jobcoinmixer.app.dto.TransferRequest;
import com.jobcoinmixer.app.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping(Paths.TRANSFER_TO_HOUSE_ADDRESS)
    public ResponseEntity<ApiResponse> transferToHouseAccount() {
        BigDecimal totalAmountTransferred = transferService.transferToHouseAddress();

        // Return the success response
        ApiResponse response = new ApiResponse("success");
        return ResponseEntity.ok(response);
    }

    @PostMapping(Paths.TRANSFER_TO_DEPOSIT_ADDRESS)
    public ResponseEntity<ApiResponse> transferJobcoinsToDepositAddress(@RequestBody TransferRequest transferRequest) {
        String from = transferRequest.getFrom();
        String to = transferRequest.getTo();
        BigDecimal amount = transferRequest.getAmount();

        // Perform the transfer
        transferService.transferJobcoinsToDepositAddress(from, to, amount);

        // Return the success response
        ApiResponse response = new ApiResponse("success");
        return ResponseEntity.ok(response);
    }
}

