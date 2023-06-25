package com.jobcoinmixer.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "app")
@Configuration
public class ApplicationProperties {
    private String houseAddress;
    private String seedAmount;
}
