package com.jobcoinmixer.app.repository;

import com.jobcoinmixer.app.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    // No additional methods needed
}
