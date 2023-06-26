package com.jobcoinmixer.app.controller;

import com.jobcoinmixer.app.Paths;
import com.jobcoinmixer.app.dto.ApiResponse;
import com.jobcoinmixer.app.dto.DepositAddressResponse;
import com.jobcoinmixer.app.dto.WithdrawalAddressesRequest;
import com.jobcoinmixer.app.model.Deposit;
import com.jobcoinmixer.app.service.DepositService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final DepositService depositService;

    public UserController(DepositService depositService) {
        this.depositService = depositService;
    }

    @PostMapping(Paths.GET_DEPOSIT_ADDRESS)
    public ResponseEntity<DepositAddressResponse> generateDepositAddress(@RequestBody WithdrawalAddressesRequest request) {
        List<String> withdrawalAddresses = request.getWithdrawalAddresses();
        Deposit deposit = depositService.generateDeposit(withdrawalAddresses);
        DepositAddressResponse response = new DepositAddressResponse(deposit.getDepositAddress());
        return ResponseEntity.ok(response);
    }

}



