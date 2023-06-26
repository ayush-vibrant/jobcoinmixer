package com.jobcoinmixer.app.service;

import com.jobcoinmixer.app.model.Deposit;
import com.jobcoinmixer.app.repository.DepositRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Service class for managing deposits.
 */
@Service
@AllArgsConstructor
public class DepositService {
    private final DepositRepository depositRepository;

    /**
     * Generates a new deposit with the given withdrawal addresses.
     *
     * @param withdrawalAddresses the list of withdrawal addresses for the deposit
     * @return the generated deposit
     */
    public Deposit generateDeposit(List<String> withdrawalAddresses) {
        // Generate a deposit address
        String depositAddress = generateDepositAddress();

        // Create a new deposit instance
        Deposit deposit = new Deposit(depositAddress, withdrawalAddresses, BigDecimal.ZERO);
        deposit.setWithdrawalAddresses(withdrawalAddresses);

        // Save the deposit to the database
        return depositRepository.save(deposit);
    }

    /**
     * Generates a unique deposit address.
     *
     * @return the generated deposit address
     */
    private String generateDepositAddress() {
        // In Jobcoin, an "address" is just an arbitrary string
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

    /**
     * Retrieves all deposits.
     *
     * @return the list of all deposits
     */
    public List<Deposit> getAllDeposits() {
        return depositRepository.findAll();
    }

    /**
     * Calculates the total amount from a list of deposits.
     *
     * @param deposits the list of deposits
     * @return the total amount
     */
    public BigDecimal calculateTotalAmount(List<Deposit> deposits) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Deposit deposit : deposits) {
            totalAmount = totalAmount.add(deposit.getAmount());
        }
        return totalAmount;
    }

    /**
     * Retrieves a deposit by its deposit address.
     *
     * @param depositAddress the deposit address
     * @return the corresponding deposit, or null if not found
     */
    public Deposit getDepositByAddress(String depositAddress) {
        return depositRepository.findByDepositAddress(depositAddress);
    }

    /**
     * Saves a deposit.
     *
     * @param deposit the deposit to save
     */
    public void save(Deposit deposit) {
        depositRepository.save(deposit);
    }
}
