package com.jobcoinmixer.app.service;

import com.jobcoinmixer.app.config.ApplicationProperties;
import com.jobcoinmixer.app.model.HouseAccount;
import com.jobcoinmixer.app.repository.HouseAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class HouseAccountService {
    private final HouseAccountRepository houseAccountRepository;
    private final ApplicationProperties applicationProperties;

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



