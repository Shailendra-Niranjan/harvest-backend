package com.harvestbackend.repository;

import com.harvestbackend.model.User;
import com.harvestbackend.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet , Long> {
    Optional<Wallet> findByUser(User user);
}
