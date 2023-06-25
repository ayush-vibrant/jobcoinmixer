package com.jobcoinmixer.app.repository;

import com.jobcoinmixer.app.model.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {
    Optional<Fee> findByDepositAddress(String depositAddress);

    // additional methods if needed
}

