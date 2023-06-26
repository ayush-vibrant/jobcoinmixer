package com.jobcoinmixer.app.repository;

import com.jobcoinmixer.app.model.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Fee entities in the database.
 */
@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {

    /**
     * Retrieves a Fee entity by deposit address.
     *
     * @param depositAddress The deposit address to search for.
     * @return An Optional containing the Fee entity if found, or an empty Optional if not found.
     */
    Optional<Fee> findByDepositAddress(String depositAddress);

    // additional methods if needed
}
