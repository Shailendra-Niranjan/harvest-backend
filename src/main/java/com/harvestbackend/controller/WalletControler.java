package com.harvestbackend.controller;

import com.harvestbackend.model.Wallet;
import com.harvestbackend.payload.response.MessageResponse;
import com.harvestbackend.repository.WalletRepository;
import com.harvestbackend.services.WalletService;
import org.eclipse.angus.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletControler {
    @Autowired
    WalletService walletService;

    @PostMapping("/updatemoney")
    public ResponseEntity<?> updateMoney(@RequestParam(name = "money") Double money) {
        Double money1 = walletService.updateMoney(money);
        return ResponseEntity.ok(money1);
    }

    @GetMapping("/getbalance")
    public ResponseEntity<?> getBalance() {
        Double money = walletService.getMoney();
        return ResponseEntity.ok(money);
    }
}
