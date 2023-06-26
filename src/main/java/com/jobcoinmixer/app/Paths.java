package com.jobcoinmixer.app;

/**
 * Interface that defines the API paths for the Jobcoin Mixer application.
 */
public interface Paths {
    /**
     * API path for transferring Jobcoins to the house address.
     */
    String TRANSFER_TO_HOUSE_ADDRESS = "/transfer-to-house-address";

    /**
     * API path for transferring Jobcoins to a deposit address.
     */
    String TRANSFER_TO_DEPOSIT_ADDRESS = "/transfer-to-deposit-address";

    /**
     * API path for initiating a withdrawal of Jobcoins.
     */
    String WITHDRAW_JOB_COINS = "/withdraw";

    /**
     * API path for generating deposit addresses for users.
     */
    String GET_DEPOSIT_ADDRESS = "/users/addresses";
}
