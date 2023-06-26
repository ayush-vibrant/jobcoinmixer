package com.jobcoinmixer.app.repository;

import com.jobcoinmixer.app.dto.DepositStatus;
import com.jobcoinmixer.app.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Deposit entities in the database.
 */
@Repository
public interface DepositRepository extends JpaRepository<Deposit, String> {

    /**
     * Retrieves a Deposit entity by deposit address.
     *
     * @param depositAddress The deposit address to search for.
     * @return The Deposit entity if found, or null if not found.
     */
    Deposit findByDepositAddress(String depositAddress);
    List<Deposit> findByStatus(DepositStatus status);
    void updateStatusByDepositAddress(String depositAddress, DepositStatus newStatus);

}
