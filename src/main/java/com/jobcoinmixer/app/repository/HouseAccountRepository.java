package com.jobcoinmixer.app.repository;

import com.jobcoinmixer.app.model.HouseAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing HouseAccount entities in the database.
 */
@Repository
public interface HouseAccountRepository extends JpaRepository<HouseAccount, String> {
    // No additional methods needed
}
