package com.jobcoinmixer.app.controller;

import com.jobcoinmixer.app.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WithdrawalController {
    private final TransferService transferService;

    public WithdrawalController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PutMapping("/withdraw")
    public ResponseEntity<Void> initiateWithdrawal(@RequestParam("deposit_address") String depositAddress) {
        transferService.initiateWithdrawal(depositAddress);
        return ResponseEntity.ok().build();
    }

    // other endpoints
}
