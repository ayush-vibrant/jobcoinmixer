package com.jobcoinmixer.app.exception;

public class DepositNotFoundException extends RuntimeException {
    public DepositNotFoundException(String depositAddress) {
        super("Deposit not found for deposit address: " + depositAddress);
    }
}
