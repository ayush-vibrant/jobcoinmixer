package com.jobcoinmixer.app.controller;

import com.jobcoinmixer.app.Paths;
import com.jobcoinmixer.app.dto.DepositAddressResponse;
import com.jobcoinmixer.app.dto.WithdrawalAddressesRequest;
import com.jobcoinmixer.app.model.Deposit;
import com.jobcoinmixer.app.service.DepositService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final DepositService depositService;

    /**
     * Constructs a new UserController with the given DepositService.
     *
     * @param depositService the DepositService to be used for generating deposit addresses
     */
    public UserController(DepositService depositService) {
        this.depositService = depositService;
    }

    /**
     * Generates a deposit address for the given withdrawal addresses.
     *
     * @param request the WithdrawalAddressesRequest containing the withdrawal addresses
     * @return a ResponseEntity containing the DepositAddressResponse with the generated deposit address
     */
    @PostMapping(Paths.GET_DEPOSIT_ADDRESS)
    public ResponseEntity<DepositAddressResponse> generateDepositAddress(@RequestBody WithdrawalAddressesRequest request) {
        List<String> withdrawalAddresses = request.getWithdrawalAddresses();
        Deposit deposit = depositService.generateDeposit(withdrawalAddresses);
        DepositAddressResponse response = new DepositAddressResponse(deposit.getDepositAddress());
        return ResponseEntity.ok(response);
    }
}
