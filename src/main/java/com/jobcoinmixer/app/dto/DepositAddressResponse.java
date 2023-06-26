package com.jobcoinmixer.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) representing a response containing a deposit address in the Jobcoin mixing application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositAddressResponse {
    /**
     * The deposit address generated for a user in the Jobcoin mixing application.
     */
    private String depositAddress;
}
