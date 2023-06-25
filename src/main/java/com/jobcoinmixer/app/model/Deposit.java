package com.jobcoinmixer.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "deposit_table")
public class Deposit {
    @Id
    @Column(name = "deposit_address")
    private String depositAddress;

    @ElementCollection
    @CollectionTable(name = "withdrawal_addresses", joinColumns = @JoinColumn(name = "deposit_address"))
    @Column(name = "withdrawal_address")
    private List<String> withdrawalAddresses;

    @Column(name = "amount")
    private Integer amount;

}

