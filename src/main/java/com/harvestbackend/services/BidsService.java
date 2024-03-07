package com.harvestbackend.services;

import com.harvestbackend.model.Bids;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BidsService {

    String  createBids(int price , Long product_id);
    List<Bids> getAllBidOfUser();
    Bids getTopBid(Long id);
    Bids updateBid(int price , Long bids_id);
}
