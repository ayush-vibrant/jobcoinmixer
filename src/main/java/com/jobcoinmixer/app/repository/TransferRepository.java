package com.jobcoinmixer.app.repository;

import com.jobcoinmixer.app.dto.WithdrawalDetail;
import com.jobcoinmixer.app.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    // No additional methods needed
    List<Transfer> findAllByDepositAddress(String depositAddress);
}
