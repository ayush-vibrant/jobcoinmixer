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

    /**
     * Constructs a new TransferController with the given TransferService.
     *
     * @param transferService the TransferService to be used for performing transfers
     */
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    /**
     * Transfers Jobcoins to the house account.
     *
     * NOTE: Ideally in production, this should be a scheduled operation. For now, I am mimicking this behavior by API call.
     *
     * @return A ResponseEntity containing the ApiResponse indicating the success of the transfer
     */
    @PostMapping(Paths.TRANSFER_TO_HOUSE_ADDRESS)
    public ResponseEntity<ApiResponse> transferToHouseAccount() {
        BigDecimal totalAmountTransferred = transferService.transferToHouseAddress();

        // Return the success response
        ApiResponse response = new ApiResponse("success");
        return ResponseEntity.ok(response);
    }

    /**
     * Transfers Jobcoins from one address to a deposit address.
     *
     * @param transferRequest the TransferRequest containing the transfer details
     * @return a ResponseEntity containing the ApiResponse indicating the success of the transfer
     */
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
