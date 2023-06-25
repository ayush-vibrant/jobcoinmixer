package com.jobcoinmixer.app.repository;

import com.jobcoinmixer.app.model.Deposit;
import com.jobcoinmixer.app.model.Fee;
import com.jobcoinmixer.app.model.HouseAccount;
import com.jobcoinmixer.app.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, String> {
    Deposit findByDepositAddress(String depositAddress);
}

