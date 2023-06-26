package com.jobcoinmixer.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a request for withdrawal addresses in the Jobcoin mixing application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalAddressesRequest {
    @JsonProperty("withdrawal_addresses")
    private List<String> withdrawalAddresses;
}
