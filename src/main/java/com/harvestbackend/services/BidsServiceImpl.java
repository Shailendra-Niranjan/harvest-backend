package com.harvestbackend.services;

import com.harvestbackend.model.Bids;
import com.harvestbackend.model.Farmer;
import com.harvestbackend.model.Product;
import com.harvestbackend.model.User;
import com.harvestbackend.repository.BidsRepository;
import com.harvestbackend.repository.FarmerRepository;
import com.harvestbackend.repository.ProductRepository;
import com.harvestbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BidsServiceImpl implements BidsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    FarmerRepository farmerRepository;
    @Autowired
    BidsRepository bidsRepository;
    @Autowired
    ProductRepository productRepository;
    @Override
    public String  createBids(int price, Long product_id){
        Product product = productRepository.findById(product_id).orElseThrow(()->new RuntimeException("product are not found " +product_id));
        String  userDetails1 = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userDetails1).orElseThrow(() -> new RuntimeException("User not found"+ userDetails1));
        Farmer farmer = product.getFarmer();
        Bids bids = new Bids();
        if(price<product.getStartingBid()){
            return "price must  be greater than starting bid  ";
        }
        if(price<=(product.getTopBid()+product.getIncrement())){
            return "price must be greater than  + "+(product.getTopBid() + product.getIncrement());
        }
        if(!user.getUsername().equals(farmer.getUser().getUsername())){
            product.setTopBid(price);
            bids.setPrice(price);
            bids.setUser(user);
            bids.setFarmer(farmer);
            bids.setProduct(productRepository.save(product));
            bids.setDate(new Date());
            Bids bids1 =  bidsRepository.save(bids);
            return  "Bid on product "+ product.getName() +" farmer" +farmer.getUser().getFirstName();
        }
        return  "you can't bid on own product";
    }

    @Override
    public List<Bids> getAllBidOfUser() {
        String  userDetails1 = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userDetails1).orElseThrow(() -> new RuntimeException("User not found"+ userDetails1));
        List<Bids> res = bidsRepository.findByUser(user);
        return res;
    }

    @Override
    public Bids updateBid(int price , Long bids_id) {
        String  userDetails1 = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userDetails1).orElseThrow(() -> new RuntimeException("User not found"+ userDetails1));
        Optional<Bids> bids = bidsRepository.findById(bids_id);
        User  user1 = bids.get().getUser();
        if(user1.equals(user)) {
            bids.get().setDate(new Date());
            bids.get().setPrice(price);
            Bids bids1 = bidsRepository.save(bids.get());
            return bids1;
        }
        return new Bids();
    }
    public String deleteBid(Long id){
        Bids bids = bidsRepository.findById(id).orElseThrow(()->new RuntimeException("bids are not found"));
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName).orElseThrow(()->new RuntimeException("user not found "));
        if(bids.getUser().equals(user)){
            bidsRepository.delete(bids);
        }
        return "you are not able to delete other user bids";

    }
}
