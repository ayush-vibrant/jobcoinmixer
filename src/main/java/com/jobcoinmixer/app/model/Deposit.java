package com.jobcoinmixer.app.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deposit_table")
public class Deposit {
    @Id
    @Column(name = "deposit_address")
    private String depositAddress;

//    @ElementCollection
//    @Column(name = "withdrawal_address")
//    @CollectionTable(name = "withdrawal_addresses", joinColumns = @JoinColumn(name = "deposit_address"))
//    private List<String> withdrawalAddresses;

    @Column(name = "withdrawal_addresses")
    private List<String> withdrawalAddresses;

    @Column(name = "amount")
    private BigDecimal amount;

}


