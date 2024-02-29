package com.harvestbackend.services;

import com.harvestbackend.model.Farmer;
import com.harvestbackend.model.Product;
import com.harvestbackend.model.User;
import com.harvestbackend.payload.request.FarmerSignUpRequest;
import com.harvestbackend.repository.FarmerRepository;
import com.harvestbackend.repository.ProductRepository;
import com.harvestbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FarmerServiceImpl implements FarmerService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    FarmerRepository farmerRepository;
    @Autowired
    ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(FarmerServiceImpl.class);
    @Override
    public String userToFarmer(FarmerSignUpRequest farmerSignUpRequest) {
//        logger.debug("error: ", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String  userDetails1 = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userDetails1).orElseThrow(() -> new RuntimeException("User not found"+ userDetails1));
        user.setIsFarmer(true);
        Farmer farmer = new Farmer();
      User user1  = userRepository.save(user);
        farmer.setUser(user1);
        farmer.setEarning(farmerSignUpRequest.getEarning());

        farmerRepository.save(farmer);

        return "User converted into farmer by username: " + userDetails1;
    }

    @Override
    public List<Farmer> getALL() {
        return farmerRepository.findAll();
    }

    @Override
    public List<Product> getAllProductOfFarmer() {
        String  userDetails1 = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userDetails1).orElseThrow(() -> new RuntimeException("User not found"+ userDetails1));
        Farmer farmer = farmerRepository.findByUser(user);
        List<Product> res = productRepository.findByFarmer(farmer);
        return res;
    }


}
