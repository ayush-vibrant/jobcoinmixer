package com.jobcoinmixer.app.service;

import com.jobcoinmixer.app.config.ApplicationProperties;
import com.jobcoinmixer.app.model.HouseAccount;
import com.jobcoinmixer.app.repository.HouseAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service class for managing the house account.
 */
@Service
@AllArgsConstructor
public class HouseAccountService {
    private final HouseAccountRepository houseAccountRepository;
    private final ApplicationProperties applicationProperties;

    /**
     * Adds the specified amount to the house account's total amount.
     *
     * @param totalAmountTransferred the amount to be added to the house account
     * @throws java.util.NoSuchElementException if the house account is not found
     */
    public void addAmountToHouseAccount(BigDecimal totalAmountTransferred) {
        // Fetch the house account from the database
        String houseAddress = applicationProperties.getHouseAddress();
        HouseAccount houseAccount = houseAccountRepository.findById(houseAddress).orElseThrow();

        BigDecimal currentTotalAmount = houseAccount.getTotalAmount();
        BigDecimal updatedTotalAmount = currentTotalAmount.add(totalAmountTransferred);

        houseAccount.setTotalAmount(updatedTotalAmount);

        // Save the updated house account to the database
        houseAccountRepository.save(houseAccount);
    }
}
