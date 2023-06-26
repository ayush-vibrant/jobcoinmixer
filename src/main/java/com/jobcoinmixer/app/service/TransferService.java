package com.jobcoinmixer.app.service;

import com.jobcoinmixer.app.config.ApplicationProperties;
import com.jobcoinmixer.app.dto.TransferStatus;
import com.jobcoinmixer.app.dto.WithdrawalDetail;
import com.jobcoinmixer.app.exception.DepositNotFoundException;
import com.jobcoinmixer.app.model.Deposit;
import com.jobcoinmixer.app.model.Fee;
import com.jobcoinmixer.app.model.Transfer;
import com.jobcoinmixer.app.repository.TransferRepository;
import com.jobcoinmixer.app.service.utils.FeeCalculator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Service class for managing transfers of Jobcoins.
 */
@Service
@AllArgsConstructor
public class TransferService {
    private final TransferRepository transferRepository;
    private final DepositService depositService;
    private final HouseAccountService houseAccountService;
    private final FeeService feeService;
    private final FeeCalculator feeCalculator;

    @Autowired
    private final ApplicationProperties applicationProperties;

    /**
     * Initiates the withdrawal process for the specified deposit address.
     *
     * @param depositAddress the deposit address
     * @throws DepositNotFoundException if the deposit is not found
     */
    public void initiateWithdrawal(String depositAddress) throws DepositNotFoundException {
        // Fetch the deposit from the database
        Deposit deposit = depositService.getDepositByAddress(depositAddress);

        if (deposit != null) {
            // Fetch the withdrawal addresses and amount from the deposit
            List<String> withdrawalAddresses = deposit.getWithdrawalAddresses();
            BigDecimal amount = deposit.getAmount();

            // Deduct fees based on the defined strategy
            BigDecimal fee = feeCalculator.calculateFee(amount, withdrawalAddresses.size());

            // Update the total fee in the fee collection table
            recordFeeCollection(depositAddress, fee);

            BigDecimal remainingAmount = amount.subtract(fee);

            // Transfer the remaining Jobcoins to the withdrawal addresses
            transferToWithdrawalAddresses(depositAddress, remainingAmount, withdrawalAddresses);
        } else {
            throw new DepositNotFoundException("Deposit not found");
        }
    }

    /**
     * Records the fee collection for the specified deposit address.
     *
     * @param depositAddress the deposit address
     * @param fee            the fee amount
     */

    private void recordFeeCollection(String depositAddress, BigDecimal fee) {
        Fee feeRecord = new Fee();
        feeRecord.setDepositAddress(depositAddress);
        feeRecord.setTotalFee(fee);
        feeService.saveFee(feeRecord);
    }


    /**
     * Transfers the specified amount to the withdrawal addresses.
     *
     * @param depositAddress      the deposit address
     * @param amount              the amount to be transferred
     * @param withdrawalAddresses the withdrawal addresses
     */
    private void transferToWithdrawalAddresses(String depositAddress, BigDecimal amount, List<String> withdrawalAddresses) {
        BigDecimal remainingAmount = amount;
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Random random = new Random();

        for (int i = 0; i < withdrawalAddresses.size(); i++) {
            String withdrawalAddress = withdrawalAddresses.get(i);
            BigDecimal installmentAmount = generateInstallmentAmount(remainingAmount,
                    withdrawalAddresses.size() - i, random);
            remainingAmount = remainingAmount.subtract(installmentAmount);

            int delaySeconds = i / 100;
            executorService.schedule(() -> {
                transferJobcoinsToWithdrawalAddress(depositAddress, applicationProperties.getHouseAddress(),
                        withdrawalAddress, installmentAmount);
            }, delaySeconds, TimeUnit.SECONDS);
        }

        executorService.shutdown();
    }

    /**
     * Transfers the specified amount of Jobcoins from the house address to the withdrawal address.
     *
     * @param depositAddress     the deposit address
     * @param houseAddress       the house address
     * @param withdrawalAddress  the withdrawal address
     * @param installmentAmount  the amount of Jobcoins to be transferred
     */

    private void transferJobcoinsToWithdrawalAddress(String depositAddress, String houseAddress,
                                                     String withdrawalAddress, BigDecimal installmentAmount) {
        // Should use SLF4J for logging instead of System.out.println
        System.out.println("Transferring " + installmentAmount + " Jobcoins from " + houseAddress + " to " + withdrawalAddress);

        // Update the transfer table
        updateTransferTable(withdrawalAddress, installmentAmount, TransferStatus.COMPLETED, depositAddress);
    }

    /**
     * Generates an installment amount for transferring Jobcoins to a withdrawal address.
     *
     * @param remainingAmount    the remaining amount to be transferred
     * @param remainingAddresses the number of remaining withdrawal addresses
     * @param random             the random number generator
     * @return the generated installment amount
     */
    private BigDecimal generateInstallmentAmount(BigDecimal remainingAmount, int remainingAddresses, Random random) {
        if (remainingAddresses == 1) {
            // Return the remaining amount for the last address
            return remainingAmount;
        }

        BigDecimal maxInstallmentAmount = remainingAmount.multiply(BigDecimal.valueOf(0.9));
        BigDecimal minInstallmentAmount = remainingAmount.multiply(BigDecimal.valueOf(0.1))
                .divide(BigDecimal.valueOf(remainingAddresses), BigDecimal.ROUND_DOWN);

        BigDecimal installmentAmount;
        do {
            installmentAmount = minInstallmentAmount.add(BigDecimal.valueOf(random.nextDouble())
                    .multiply(maxInstallmentAmount.subtract(minInstallmentAmount)));
            installmentAmount = installmentAmount.setScale(8, BigDecimal.ROUND_DOWN);
        } while (installmentAmount.compareTo(remainingAmount) > 0);

        return installmentAmount;
    }

    /**
     * Updates the transfer table with the details of a transfer.
     *
     * @param withdrawalWalletAddress the withdrawal wallet address
     * @param amount                  the transferred amount
     * @param status                  the status of the transfer
     * @param depositAddress          the deposit address
     */
    private void updateTransferTable(String withdrawalWalletAddress, BigDecimal amount,
                                     TransferStatus status, String depositAddress) {
        Transfer transfer = new Transfer();
        transfer.setWithdrawalWalletAddress(withdrawalWalletAddress);
        transfer.setAmount(amount);
        transfer.setStatus(String.valueOf(status));
        transfer.setDepositAddress(depositAddress);
        transferRepository.save(transfer);
    }

    /**
     * Transfers the specified amount of Jobcoins from one address to a deposit address.
     *
     * @param from   the address to transfer Jobcoins from
     * @param to     the deposit address to transfer Jobcoins to
     * @param amount the amount of Jobcoins to be transferred
     * @throws DepositNotFoundException if the deposit address is not found
     */
    public void transferJobcoinsToDepositAddress(String from, String to, BigDecimal amount) {
        // Update the deposit amount for the specified deposit address
        Deposit deposit = depositService.getDepositByAddress(to);
        if (deposit != null) {
            BigDecimal currentAmount = deposit.getAmount();
            BigDecimal newAmount = currentAmount.add(amount);
            deposit.setAmount(newAmount);
            depositService.save(deposit);
        } else {
            throw new DepositNotFoundException("Deposit address not found: " + to);
        }

        // For now, we are just logging the transfer details
        System.out.println("Transferred Jobcoins - From: " + from + " To: " + to + " Amount: " + amount);
    }

    /**
     * Transfers all Jobcoins from the deposits to the house address.
     *
     * @return the total amount of Jobcoins transferred to the house address
     */

    public BigDecimal transferToHouseAddress() {
        // Get all the deposits
        List<Deposit> deposits = depositService.getAllDeposits();

        // Get the total amount from the deposits
        BigDecimal totalAmountTransferred = depositService.calculateTotalAmount(deposits);

        // Update the house account with the transferred amount
        houseAccountService.addAmountToHouseAccount(totalAmountTransferred);
        System.out.println("Transferred " + totalAmountTransferred +
                " Jobcoins to house address: " + applicationProperties.getHouseAddress());

        // Log the transfer details for each deposit address
        deposits.forEach(deposit ->
                System.out.println("Transferred amount from " +
                        deposit.getDepositAddress() + " to house address: " + deposit.getAmount())
        );

        return totalAmountTransferred;
    }

    /**
     * Retrieves the transfer details for a specific deposit address.
     *
     * @param depositAddress the deposit address
     * @return a list of withdrawal details for the specified deposit address
     */
    public List<WithdrawalDetail> getTransferDetails(String depositAddress) {
        // find all transfers for the deposit address
        List<Transfer> allByDepositAddress = transferRepository.findAllByDepositAddress(depositAddress);

        return allByDepositAddress.stream()
                .map(transfer -> {
                    WithdrawalDetail withdrawalDetail = new WithdrawalDetail();
                    withdrawalDetail.setWithdrawalAddress(transfer.getWithdrawalWalletAddress());
                    withdrawalDetail.setAmount(transfer.getAmount());
                    withdrawalDetail.setStatus(transfer.getStatus());
                    return withdrawalDetail;
                })
                .collect(Collectors.toList());
    }

}


