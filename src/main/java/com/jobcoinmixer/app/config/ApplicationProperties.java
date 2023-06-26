package com.jobcoinmixer.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for application properties.
 */
@Data
@ConfigurationProperties(prefix = "app")
@Configuration
public class ApplicationProperties {
    /**
     * The house address used for Jobcoin transfers.
     */
    private String houseAddress;

    /**
     * The seed amount to initialize the house account with.
     */
    private String seedAmount;
}
