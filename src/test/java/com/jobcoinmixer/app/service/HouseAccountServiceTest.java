package com.jobcoinmixer.app.service;

import com.jobcoinmixer.app.config.ApplicationProperties;
import com.jobcoinmixer.app.model.HouseAccount;
import com.jobcoinmixer.app.repository.HouseAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class HouseAccountServiceTest {
    @Mock
    private HouseAccountRepository houseAccountRepository;

    @Mock
    private ApplicationProperties applicationProperties;

    private HouseAccountService houseAccountService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        houseAccountService = new HouseAccountService(houseAccountRepository, applicationProperties);
    }

    @Test
    void testAddAmountToHouseAccount() {
        // Mock the application properties
        String houseAddress = "house_address";
        BigDecimal totalAmountTransferred = new BigDecimal("100.00");
        when(applicationProperties.getHouseAddress()).thenReturn(houseAddress);

        // Mock the house account repository
        HouseAccount houseAccount = new HouseAccount();
        houseAccount.setHouseAddress(houseAddress);
        houseAccount.setTotalAmount(new BigDecimal("500.00"));
        when(houseAccountRepository.findById(houseAddress)).thenReturn(Optional.of(houseAccount));

        // Perform the addAmountToHouseAccount operation
        houseAccountService.addAmountToHouseAccount(totalAmountTransferred);

        // Verify that the house account was updated and saved
        verify(houseAccountRepository, times(1)).save(houseAccount);
        assertEquals(new BigDecimal("600.00"), houseAccount.getTotalAmount());
    }
}
