package com.jobcoinmixer.app.repository;

import com.jobcoinmixer.app.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Transfer entities in the database.
 */
@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    /**
     * Retrieves a list of Transfer entities associated with the specified deposit address.
     *
     * @param depositAddress The deposit address
     * @return A list of Transfer entities
     */
    List<Transfer> findAllByDepositAddress(String depositAddress);
}
