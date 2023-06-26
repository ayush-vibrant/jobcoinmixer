package com.jobcoinmixer.app.service;

import com.jobcoinmixer.app.model.Fee;
import com.jobcoinmixer.app.repository.FeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FeeServiceTest {

    @Mock
    private FeeRepository feeRepository;

    @InjectMocks
    private FeeService feeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateTotalFee() {
        // Prepare test data
        String depositAddress = "address1";
        BigDecimal fee = new BigDecimal("10.00");
        Fee feeRecord = new Fee(depositAddress, fee);

        // Mock repository method
        when(feeRepository.findByDepositAddress(depositAddress)).thenReturn(Optional.of(feeRecord));
        when(feeRepository.save(any(Fee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Update total fee
        feeService.updateTotalFee(depositAddress, fee);

        // Verify repository method called
        verify(feeRepository, times(1)).findByDepositAddress(depositAddress);
        verify(feeRepository, times(1)).save(any(Fee.class));

        // Assert fee record
        assertEquals(fee, feeRecord.getTotalFee());
    }

}
