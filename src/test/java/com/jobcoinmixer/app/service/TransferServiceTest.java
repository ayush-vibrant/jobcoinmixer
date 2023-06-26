package com.jobcoinmixer.app.service;

import com.jobcoinmixer.app.config.ApplicationProperties;
import com.jobcoinmixer.app.dto.DepositStatus;
import com.jobcoinmixer.app.dto.TransferStatus;
import com.jobcoinmixer.app.dto.WithdrawalDetail;
import com.jobcoinmixer.app.exception.DepositNotFoundException;
import com.jobcoinmixer.app.model.Deposit;
import com.jobcoinmixer.app.model.Fee;
import com.jobcoinmixer.app.model.Transfer;
import com.jobcoinmixer.app.repository.TransferRepository;
import com.jobcoinmixer.app.service.utils.FeeCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransferServiceTest {
    @Mock
    private TransferRepository transferRepository;

    @Mock
    private DepositService depositService;

    @Mock
    private HouseAccountService houseAccountService;

    @Mock
    private FeeService feeService;

    @Mock
    private FeeCalculator feeCalculator;

    @Mock
    private ApplicationProperties applicationProperties;

    private TransferService transferService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        transferService = new TransferService(
                transferRepository,
                depositService,
                houseAccountService,
                feeService,
                feeCalculator,
                applicationProperties
        );
    }

    @Test
    void testInitiateWithdrawalWithValidDepositAddress() throws DepositNotFoundException {
        // Mock the deposit service
        String depositAddress = "deposit_address";
        Deposit deposit = new Deposit();
        deposit.setWithdrawalAddresses(new ArrayList<>());
        deposit.setAmount(new BigDecimal("100.00"));
        deposit.setStatus(DepositStatus.MOVED_TO_HOUSE);
        when(depositService.getDepositByAddress(depositAddress)).thenReturn(deposit);

        // Mock the fee calculator
        BigDecimal fee = new BigDecimal("10.00");
        when(feeCalculator.calculateFee(deposit.getAmount(), deposit.getWithdrawalAddresses().size())).thenReturn(fee);

        // Mock the application properties
        String houseAddress = "house_address";
        when(applicationProperties.getHouseAddress()).thenReturn(houseAddress);

        // Perform the initiateWithdrawal operation
        transferService.initiateWithdrawal(depositAddress);

        // Verify the method calls
        verify(depositService, times(1)).getDepositByAddress(depositAddress);
        verify(feeCalculator, times(1)).calculateFee(deposit.getAmount(), deposit.getWithdrawalAddresses().size());
        verify(feeService, times(1)).saveFee(any(Fee.class));
//        verify(transferRepository, times(1)).save(any(Transfer.class));
//        verify(houseAccountService, times(1)).addAmountToHouseAccount(deposit.getAmount().subtract(fee));
        verify(depositService, times(1)).getDepositByAddress(depositAddress);
    }

    @Test
    void testInitiateWithdrawalWithInvalidDepositAddress() {
        // Mock the deposit service to return null
        String depositAddress = "non_existing_address";
        when(depositService.getDepositByAddress(depositAddress)).thenReturn(null);

        // Perform the initiateWithdrawal operation and expect DepositNotFoundException
        assertThrows(DepositNotFoundException.class, () -> transferService.initiateWithdrawal(depositAddress));

        // Verify the method calls
        verify(depositService, times(1)).getDepositByAddress(depositAddress);
        verifyNoInteractions(feeCalculator, transferRepository, feeService, houseAccountService);
    }

    @Test
    void testTransferJobcoinsToWithdrawalAddress() {
        // Mock the application properties
        String houseAddress = "house_address";
        when(applicationProperties.getHouseAddress()).thenReturn(houseAddress);

        // Perform the transferJobcoinsToWithdrawalAddress operation
        String depositAddress = "deposit_address";
        String withdrawalAddress = "withdrawal_address";
        BigDecimal installmentAmount = new BigDecimal("50.00");
        transferService.transferJobcoinsToWithdrawalAddress(depositAddress, houseAddress, withdrawalAddress, installmentAmount);

        // Verify the method calls
        verify(transferRepository, times(1)).save(any(Transfer.class));
    }

    @Test
    void testGenerateInstallmentAmount() {
        BigDecimal remainingAmount = new BigDecimal("100.00");
        int remainingAddresses = 3;
        Random random = new Random();

        BigDecimal installmentAmount = transferService.generateInstallmentAmount(remainingAmount, remainingAddresses, random);

        // Verify that the generated installment amount is within the valid range
        BigDecimal maxInstallmentAmount = remainingAmount.multiply(BigDecimal.valueOf(0.9));
        BigDecimal minInstallmentAmount = remainingAmount.multiply(BigDecimal.valueOf(0.1))
                .divide(BigDecimal.valueOf(remainingAddresses), BigDecimal.ROUND_DOWN);
        assertTrue(installmentAmount.compareTo(minInstallmentAmount) >= 0);
        assertTrue(installmentAmount.compareTo(maxInstallmentAmount) <= 0);
    }

    @Test
    void testUpdateTransferTable() {
        // Perform the updateTransferTable operation
        String withdrawalWalletAddress = "withdrawal_address";
        BigDecimal amount = new BigDecimal("50.00");
        TransferStatus status = TransferStatus.COMPLETED;
        String depositAddress = "deposit_address";
        transferService.updateTransferTable(withdrawalWalletAddress, amount, status, depositAddress);

        // Verify the method call
        verify(transferRepository, times(1)).save(any(Transfer.class));
    }

    @Test
    void testTransferJobcoinsToDepositAddressWithValidDepositAddress() throws DepositNotFoundException {
        // Mock the deposit service
        String depositAddress = "deposit_address";
        Deposit deposit = new Deposit();
        deposit.setAmount(new BigDecimal("100.00"));
        when(depositService.getDepositByAddress(depositAddress)).thenReturn(deposit);

        // Perform the transferJobcoinsToDepositAddress operation
        String from = "from_address";
        String to = "to_address";
        BigDecimal amount = new BigDecimal("50.00");
        transferService.transferJobcoinsToDepositAddress(from, depositAddress, amount);

        // Verify the method calls
        verify(depositService, times(1)).getDepositByAddress(depositAddress);
        verify(depositService, times(1)).save(deposit);
    }

    @Test
    void testTransferJobcoinsToDepositAddressWithInvalidDepositAddress() {
        // Mock the deposit service
        String depositAddress = "invalid_deposit_address";
        when(depositService.getDepositByAddress(depositAddress)).thenReturn(null);

        // Perform the transferJobcoinsToDepositAddress operation
        String from = "from_address";
        String to = "to_address";
        BigDecimal amount = new BigDecimal("50.00");

        assertThrows(DepositNotFoundException.class,
                () -> transferService.transferJobcoinsToDepositAddress(from, depositAddress, amount));

        // Verify the method calls
        verify(depositService, times(1)).getDepositByAddress(depositAddress);
        verifyNoMoreInteractions(depositService);
    }

    @Test
    void testTransferToHouseAddress() {
        // Mock the deposit service
        List<Deposit> deposits = new ArrayList<>(List.of(
                createDeposit("address1", new BigDecimal("50.00"), DepositStatus.TRANSFERRED),
                createDeposit("address2", new BigDecimal("75.00"), DepositStatus.TRANSFERRED),
                createDeposit("address3", new BigDecimal("100.00"), DepositStatus.TRANSFERRED)
        ));
        when(depositService.getDepositsByStatus(DepositStatus.TRANSFERRED)).thenReturn(deposits);

        // Perform the transferToHouseAddress operation
        BigDecimal totalAmountTransferred = transferService.transferToHouseAddress();

        // Verify the method calls
        verify(depositService, times(1)).getDepositsByStatus(DepositStatus.TRANSFERRED);
        verify(houseAccountService, times(1)).addAmountToHouseAccount(totalAmountTransferred);
        verify(applicationProperties, times(1)).getHouseAddress();

        // Verify the total amount transferred
        BigDecimal expectedTotalAmountTransferred = deposits.stream()
                .map(Deposit::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(expectedTotalAmountTransferred, totalAmountTransferred);
    }

    @Test
    void testGetTransferDetails() {
        // Mock the transfer repository
        String depositAddress = "deposit_address";
        List<Transfer> transfers = List.of(
                createTransfer("address1", new BigDecimal("50.00"), TransferStatus.COMPLETED, depositAddress),
                createTransfer("address2", new BigDecimal("75.00"), TransferStatus.IN_PROGRESS, depositAddress),
                createTransfer("address3", new BigDecimal("100.00"), TransferStatus.COMPLETED, depositAddress)
        );
        when(transferRepository.findAllByDepositAddress(depositAddress)).thenReturn(transfers);

        // Perform the getTransferDetails operation
        List<WithdrawalDetail> withdrawalDetails = transferService.getTransferDetails(depositAddress);

        // Verify the method call
        verify(transferRepository, times(1)).findAllByDepositAddress(depositAddress);

        // Verify the returned withdrawal details
        assertEquals(transfers.size(), withdrawalDetails.size());
        for (int i = 0; i < transfers.size(); i++) {
            Transfer transfer = transfers.get(i);
            WithdrawalDetail withdrawalDetail = withdrawalDetails.get(i);
            assertEquals(transfer.getWithdrawalWalletAddress(), withdrawalDetail.getWithdrawalAddress());
            assertEquals(transfer.getAmount(), withdrawalDetail.getAmount());
            assertEquals(transfer.getStatus(), withdrawalDetail.getStatus());
        }
    }

    /**
     * Creates a new Deposit instance with the given deposit address and amount.
     *
     * @param depositAddress the deposit address
     * @param amount         the amount
     * @return the created Deposit instance
     */
    private Deposit createDeposit(String depositAddress, BigDecimal amount, DepositStatus status) {
        Deposit deposit = new Deposit();
        deposit.setDepositAddress(depositAddress);
        deposit.setAmount(amount);
        deposit.setStatus(status);
        return deposit;
    }

    /**
     * Creates a new Transfer instance with the given withdrawal address, amount, status, and deposit address.
     *
     * @param withdrawalAddress the withdrawal address
     * @param amount            the amount
     * @param status            the status
     * @param depositAddress    the deposit address
     * @return the created Transfer instance
     */
    private Transfer createTransfer(String withdrawalAddress, BigDecimal amount, TransferStatus status, String depositAddress) {
        Transfer transfer = new Transfer();
        transfer.setWithdrawalWalletAddress(withdrawalAddress);
        transfer.setAmount(amount);
        transfer.setStatus(String.valueOf(status));
        transfer.setDepositAddress(depositAddress);
        return transfer;
    }
}
