package com.jobcoinmixer.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a withdrawal request in the Jobcoin mixing application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalRequest {
    @JsonProperty("deposit_address")
    String depositAddress;
}
