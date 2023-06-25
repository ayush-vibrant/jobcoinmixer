package com.jobcoinmixer.app.controller;

import com.jobcoinmixer.app.dto.WithdrawalDetail;
import com.jobcoinmixer.app.dto.WithdrawalRequest;
import com.jobcoinmixer.app.exception.DepositNotFoundException;
import com.jobcoinmixer.app.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class WithdrawalController {
    private final TransferService transferService;

    public WithdrawalController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PutMapping("/withdraw")
    public ResponseEntity<List<WithdrawalDetail>> initiateWithdrawal(@RequestBody WithdrawalRequest withdrawalRequest) {
        String depositAddress = withdrawalRequest.getDepositAddress();

        try {
            transferService.initiateWithdrawal(depositAddress);

            // Retrieve the withdrawal details
            List<WithdrawalDetail> withdrawalDetails = transferService.getTransferDetails(depositAddress);

            // This might be empty. As we send jobcoin to withdrawal addresses with delay.
            // So it might happen that we don't have any transfer details yet.
            System.out.println("withdrawalDetails: " + withdrawalDetails);

            return ResponseEntity.ok(withdrawalDetails);
        } catch (DepositNotFoundException depositNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

    }

}
