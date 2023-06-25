package com.jobcoinmixer.app.service;

import com.jobcoinmixer.app.config.ApplicationProperties;
import com.jobcoinmixer.app.dto.WithdrawalDetail;
import com.jobcoinmixer.app.exception.DepositNotFoundException;
import com.jobcoinmixer.app.model.Deposit;
import com.jobcoinmixer.app.model.Fee;
import com.jobcoinmixer.app.model.Transfer;
import com.jobcoinmixer.app.repository.DepositRepository;
import com.jobcoinmixer.app.repository.FeeRepository;
import com.jobcoinmixer.app.repository.TransferRepository;
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

@Service
@AllArgsConstructor
public class TransferService {
    private final TransferRepository transferRepository;
    private final DepositService depositService;
    private final HouseAccountService houseAccountService;
    private final FeeService feeService;
    private final FeeRepository feeRepository;

    @Autowired
    private final ApplicationProperties applicationProperties;

    public void initiateWithdrawal(String depositAddress) throws DepositNotFoundException {
        // Fetch the deposit from the database
        Deposit deposit = depositService.getDepositByAddress(depositAddress);

        if (deposit != null) {
            // Fetch the withdrawal addresses and amount from the deposit
            List<String> withdrawalAddresses = deposit.getWithdrawalAddresses();
            BigDecimal amount = deposit.getAmount();

            // Deduct fees based on the defined strategy
            BigDecimal fee = calculateFee(amount, withdrawalAddresses.size());

            // Update the total fee in the fee collection table
            recordFeeCollection(depositAddress, fee);

            BigDecimal remainingAmount = amount.subtract(fee);

            // Transfer the remaining Jobcoins to the withdrawal addresses
            transferToWithdrawalAddresses(depositAddress, remainingAmount, withdrawalAddresses);
        } else {
            throw new DepositNotFoundException("Deposit not found");
        }
    }

    private void recordFeeCollection(String depositAddress, BigDecimal fee) {
        Fee feeRecord = new Fee();
        feeRecord.setDepositAddress(depositAddress);
        feeRecord.setTotalFee(fee);
        feeService.saveFee(feeRecord);
    }

    private BigDecimal calculateFee(BigDecimal amount, int numberOfAddresses) {
        // Flat fee of 1% for the first address
        BigDecimal fee = amount.multiply(BigDecimal.valueOf(0.01));

        // Incremental fee of 0.15% for additional addresses
        BigDecimal additionalFee = BigDecimal.valueOf(0.0015).multiply(BigDecimal.valueOf(numberOfAddresses - 1));
        fee = fee.add(additionalFee);

        return fee;
    }


    private void transferToWithdrawalAddresses(String depositAddress, BigDecimal amount, List<String> withdrawalAddresses) {
        BigDecimal remainingAmount = amount;
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Random random = new Random();

        for (int i = 0; i < withdrawalAddresses.size(); i++) {
            String withdrawalAddress = withdrawalAddresses.get(i);
            BigDecimal installmentAmount = generateInstallmentAmount(remainingAmount, withdrawalAddresses.size() - i, random);
            remainingAmount = remainingAmount.subtract(installmentAmount);

            int delaySeconds = i / 100;
            executorService.schedule(() -> {
                transferJobcoinsToWithdrawalAddress(depositAddress, applicationProperties.getHouseAddress(),
                        withdrawalAddress, installmentAmount);
            }, delaySeconds, TimeUnit.SECONDS);
        }

        executorService.shutdown();
    }

    private void transferJobcoinsToWithdrawalAddress(String depositAddress, String houseAddress, String withdrawalAddress, BigDecimal installmentAmount) {
        System.out.println("Transferring " + installmentAmount + " Jobcoins from " + houseAddress + " to " + withdrawalAddress);

        // Update the transfer table
        updateTransferTable(withdrawalAddress, installmentAmount, "COMPLETED", depositAddress);
    }

    private BigDecimal generateInstallmentAmount(BigDecimal remainingAmount, int remainingAddresses, Random random) {
        if (remainingAddresses == 1) {
            // Return the remaining amount for the last address
            return remainingAmount;
        }

        BigDecimal maxInstallmentAmount = remainingAmount.multiply(BigDecimal.valueOf(0.9));
        BigDecimal minInstallmentAmount = remainingAmount.multiply(BigDecimal.valueOf(0.1)).divide(BigDecimal.valueOf(remainingAddresses), BigDecimal.ROUND_DOWN);

        BigDecimal installmentAmount;
        do {
            installmentAmount = minInstallmentAmount.add(BigDecimal.valueOf(random.nextDouble()).multiply(maxInstallmentAmount.subtract(minInstallmentAmount)));
            installmentAmount = installmentAmount.setScale(8, BigDecimal.ROUND_DOWN);
        } while (installmentAmount.compareTo(remainingAmount) > 0);

        return installmentAmount;
    }

    private void updateTransferTable(String withdrawalWalletAddress, BigDecimal amount, String status, String depositAddress) {
        Transfer transfer = new Transfer();
        transfer.setWithdrawalWalletAddress(withdrawalWalletAddress);
        transfer.setAmount(amount);
        transfer.setStatus(status);
        transfer.setDepositAddress(depositAddress);
        transferRepository.save(transfer);
    }

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
                System.out.println("Transferred amount from " + deposit.getDepositAddress() + " to house address: " + deposit.getAmount())
        );

        return totalAmountTransferred;
    }

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


