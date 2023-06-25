package com.jobcoinmixer.app.service;

import com.jobcoinmixer.app.model.Deposit;
import com.jobcoinmixer.app.repository.DepositRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DepositService {
    private final DepositRepository depositRepository;

    public Deposit generateDeposit(List<String> withdrawalAddresses) {
        // Generate a deposit address
        String depositAddress = generateDepositAddress();

        // Create a new deposit instance
        Deposit deposit = new Deposit(depositAddress, withdrawalAddresses, BigDecimal.ZERO);
        deposit.setWithdrawalAddresses(withdrawalAddresses);

        // Save the deposit to the database
        return depositRepository.save(deposit);
    }

    private String generateDepositAddress() {
        // In Jobcoin an “address” is just an arbitrary string
        String newDepositAddress = UUID.randomUUID().toString();

        // Check if the new deposit address already exists in the database
        Deposit existingDeposit = depositRepository.findByDepositAddress(newDepositAddress);

        if (existingDeposit != null) {
            // The deposit address already exists, so generate a new one
            return generateDepositAddress();
        } else {
            // The deposit address does not exist, so return it
            return newDepositAddress;
        }
    }


    public List<Deposit> getAllDeposits() {
        return depositRepository.findAll();
    }

    public BigDecimal calculateTotalAmount(List<Deposit> deposits) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Deposit deposit : deposits) {
            totalAmount = totalAmount.add(deposit.getAmount());
        }
        return totalAmount;
    }

    public Deposit getDepositByAddress(String depositAddress) {
        return depositRepository.findByDepositAddress(depositAddress);
    }

    public void save(Deposit deposit) {
        depositRepository.save(deposit);
    }
}

