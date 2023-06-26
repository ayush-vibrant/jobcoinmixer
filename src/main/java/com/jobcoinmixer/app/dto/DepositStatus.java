package com.jobcoinmixer.app.dto;

public enum DepositStatus {
    CREATED("Created deposit address"),
    TRANSFERRED("User transferred to deposit address"),
    MOVED_TO_HOUSE("Moved from deposit address to house address");

    private final String description;

    DepositStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
