package com.harvestbackend.controller;

import com.harvestbackend.model.Bids;
import com.harvestbackend.repository.BidsRepository;
import com.harvestbackend.services.BidsService;
import com.harvestbackend.services.BidsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bids")
public class BidsController {
    @Autowired
    BidsService bidsService;
    @Autowired
    BidsServiceImpl bidsServiceIml;
    @PostMapping("/createbids")
    ResponseEntity<String> setBids(@RequestParam(name = "price" ) int price , @RequestParam(name = "product_id") Long product_id){
        String bids = bidsService.createBids(price , product_id);
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/topbid/{id}")
    ResponseEntity<Bids> getTopBid(@PathVariable Long id) {
        return ResponseEntity.ok(bidsService.getTopBid(id));
    }

    @PostMapping("/updatebid")
    ResponseEntity<Bids> updateBids(@RequestParam(name = "price") int price , @RequestParam(name = "bid_id") Long bid_id){
        Bids bids = bidsService.updateBid(price ,bid_id);
        return ResponseEntity.ok(bids);
    }
    @GetMapping("/deletebid")
    ResponseEntity<String> deleteBid(@RequestParam(name = "bidId") Long bidId){
        String  res = bidsServiceIml.deleteBid(bidId);
        return ResponseEntity.ok(res);
    }
}
