package com.harvestbackend.services;

import com.harvestbackend.model.Farmer;
import com.harvestbackend.model.Product;
import com.harvestbackend.payload.request.FarmerSignUpRequest;
import org.springframework.stereotype.Service;

import java.util.List;


public interface FarmerService {
    String userToFarmer(FarmerSignUpRequest farmerSignUpRequest);

    List<Farmer> getALL();
    List<Product> getAllProductOfFarmer();

}
