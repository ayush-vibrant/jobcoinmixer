package com.jobcoinmixer.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) representing a generic API response in the Jobcoin mixing application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    /**
     * The status message of the API response.
     */
    private String status;
}
