package com.harvestbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private double balance;

    // Constructors
    public Wallet() {
    }

    public Wallet(User user, double balance) {
        this.user = user;
        this.balance = balance;
    }

}
