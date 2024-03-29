package com.jobcoinmixer.app.controller;

import com.jobcoinmixer.app.Paths;
import com.jobcoinmixer.app.dto.WithdrawalDetail;
import com.jobcoinmixer.app.dto.WithdrawalRequest;
import com.jobcoinmixer.app.exception.DepositNotFoundException;
import com.jobcoinmixer.app.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class WithdrawalController {
    private final TransferService transferService;

    /**
     * Constructs a new WithdrawalController with the given TransferService.
     *
     * @param transferService the TransferService to be used for initiating withdrawals and retrieving withdrawal details
     */
    public WithdrawalController(TransferService transferService) {
        this.transferService = transferService;
    }

    /**
     * Initiates a withdrawal for the specified deposit address.
     *
     * NOTE: Ideally in production, this should be a scheduled operation or a delay based operation that
     * user chooses at the time of giving his withdrawal addresses.
     * For now, I am mimicking this behavior by API call.
     *
     *
     * @param withdrawalRequest the WithdrawalRequest containing the deposit address for withdrawal
     * @return a ResponseEntity containing the list of WithdrawalDetail indicating the success of the withdrawal
     */
    @PutMapping(Paths.WITHDRAW_JOB_COINS)
    public ResponseEntity<List<WithdrawalDetail>> initiateWithdrawal(@RequestBody WithdrawalRequest withdrawalRequest) {
        String depositAddress = withdrawalRequest.getDepositAddress();

        try {
            transferService.initiateWithdrawal(depositAddress);

            // Retrieve the withdrawal details
            List<WithdrawalDetail> withdrawalDetails = transferService.getTransferDetails(depositAddress);

            // This might be empty. As we send jobcoin to withdrawal addresses with delay.
            // So it might happen that we don't have any transfer details yet.

            // Should use SLF4J for logging instead of System.out.println
            System.out.println("withdrawalDetails: " + withdrawalDetails);

            return ResponseEntity.ok(withdrawalDetails);
        } catch (DepositNotFoundException depositNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

    }

}
