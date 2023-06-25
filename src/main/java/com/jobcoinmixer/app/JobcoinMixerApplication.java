package com.jobcoinmixer.app;

import com.jobcoinmixer.app.config.ApplicationProperties;
import com.jobcoinmixer.app.config.SeedJobcoinsInHouseAccountConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
@EnableConfigurationProperties(ApplicationProperties.class)
@Import(SeedJobcoinsInHouseAccountConfig.class)
public class JobcoinMixerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobcoinMixerApplication.class, args);
    }

}
