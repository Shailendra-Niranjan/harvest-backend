package com.harvestbackend.services;

import com.harvestbackend.model.Wallet;
import jakarta.persistence.criteria.CriteriaBuilder;

public interface WalletService {
    Double updateMoney(Double Money);
    Double getMoney();
}
