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
    public String addMoney(Integer Money) {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"+ userDetails.getUsername()));
//        Wallet wallet = new Wallet();
//        wallet.setBalance(Money);
//        wallet.setUser(user);
//
//        walletRepository.save(wallet);

        return null;
    }

    @Override
    public String updateMoney(Integer Money) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"+ userDetails.getUsername()));
        Optional<Wallet> wallet = walletRepository.findByUser(user);
        if(wallet.isPresent()){
            wallet.get().setBalance( wallet.get().getBalance()+Money);
            walletRepository.save(wallet.get());
            return "money are added to wallet by user"+userDetails.getUsername();
        }
        else {
            Wallet wallet1 = new Wallet();
            wallet1.setBalance(Money);
            wallet1.setUser(user);
            walletRepository.save(wallet1);
            return "money are added to wallet by user"+userDetails.getUsername();

        }
//        return "there is some problem in updating money";
    }
}
