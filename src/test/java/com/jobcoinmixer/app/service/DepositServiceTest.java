package com.jobcoinmixer.app.service;

import com.jobcoinmixer.app.model.Deposit;
import com.jobcoinmixer.app.repository.DepositRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class DepositServiceTest {

    @Mock
    private DepositRepository depositRepository;

    @InjectMocks
    private DepositService depositService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateDeposit() {
        // Prepare test data
        List<String> withdrawalAddresses = new ArrayList<>();
        withdrawalAddresses.add("address1");
        withdrawalAddresses.add("address2");

        // Mock repository method
        when(depositRepository.save(any(Deposit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Generate the deposit
        Deposit deposit = depositService.generateDeposit(withdrawalAddresses);

        // Verify repository method called
        verify(depositRepository, times(1)).save(any(Deposit.class));

        // Assert deposit data
        assertEquals(withdrawalAddresses, deposit.getWithdrawalAddresses());
        assertEquals(BigDecimal.ZERO, deposit.getAmount());
    }

}

