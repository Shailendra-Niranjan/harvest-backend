package com.harvestbackend.services;

import com.harvestbackend.model.User;
import com.harvestbackend.model.Wallet;
import com.harvestbackend.repository.UserRepository;
import com.harvestbackend.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Double updateMoney(Double Money) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"+ userDetails.getUsername()));
        Optional<Wallet> wallet = walletRepository.findByUser(user);
        if(wallet.isPresent()){
            wallet.get().setBalance( wallet.get().getBalance()+Money);
            walletRepository.save(wallet.get());
        }
        else {
            Wallet wallet1 = new Wallet();
            wallet1.setBalance(Money);
            wallet1.setUser(user);
            walletRepository.save(wallet1);
        }
        return getMoney();
    }

    public Double getMoney() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"+ userDetails.getUsername()));
        Optional<Wallet> wallet = walletRepository.findByUser(user);
        if (wallet.isPresent()) {
            return wallet.get().getBalance();
        }
        else {
            Wallet wallet1 = new Wallet();
            wallet1.setBalance(0.0);
            wallet1.setUser(user);
            walletRepository.save(wallet1);
            return walletRepository.findByUser(user).get().getBalance();
        }
    }

    @Override
    public Wallet getWalletForUser(User user) {
        Optional<Wallet> wallet = walletRepository.findByUser(user);
        if (wallet.isPresent()) {
            return wallet.get();
        }
        else {
            Wallet wallet1 = new Wallet();
            wallet1.setBalance(0.0);
            wallet1.setUser(user);
            return walletRepository.save(wallet1);
        }
    }

    @Override
    public Wallet updateMoneyForUser(User user, Double Money) {
        Optional<Wallet> wallet = walletRepository.findByUser(user);
        if(wallet.isPresent()){
            wallet.get().setBalance( wallet.get().getBalance()+Money);
            return walletRepository.save(wallet.get());
        }
        else {
            Wallet wallet1 = new Wallet();
            wallet1.setBalance(Money);
            wallet1.setUser(user);
            return  walletRepository.save(wallet1);
        }
    }
}
