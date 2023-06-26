package com.jobcoinmixer.app.config;

import com.jobcoinmixer.app.model.HouseAccount;
import com.jobcoinmixer.app.repository.HouseAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class SeedJobcoinsInHouseAccountConfig {

    @Autowired
    private HouseAccountRepository houseAccountRepository;

    @Autowired
    private ApplicationProperties applicationProperties;

    // Ideally in production, we should use Flyway to achieve this

    /**
     * Initializes the seed amount in the house account during application startup.
     *
     * @return a CommandLineRunner that performs the seed data initialization
     */
    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            // Check if the house account already exists
            String houseAddress = applicationProperties.getHouseAddress();
            HouseAccount existingHouseAccount = houseAccountRepository.findById(houseAddress).orElse(null);

            if (existingHouseAccount == null) {
                // Create a new house account and set the seed amount
                HouseAccount houseAccount = new HouseAccount();
                houseAccount.setHouseAddress(houseAddress);

                String seedAmount = applicationProperties.getSeedAmount();
                houseAccount.setTotalAmount(new BigDecimal(seedAmount));

                // Save the house account to the database
                houseAccountRepository.save(houseAccount);
            }
        };
    }
}
