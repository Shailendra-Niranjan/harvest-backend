package com.harvestbackend.controller;

import com.harvestbackend.repository.WalletRepository;
import com.harvestbackend.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletControler {
    @Autowired
    WalletService walletService;
}
