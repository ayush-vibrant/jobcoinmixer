package com.jobcoinmixer.app.dto;

/**
 * Enum representing the status of a transfer in the Jobcoin mixing application.
 */
public enum TransferStatus {
    /**
     * Transfer is in progress.
     */
    IN_PROGRESS,

    /**
     * Transfer has been completed successfully.
     */
    COMPLETED,

    /**
     * Transfer has failed.
     */
    FAILED;
}
