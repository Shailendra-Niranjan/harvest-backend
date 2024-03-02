package com.harvestbackend.controller;

import com.harvestbackend.model.Bids;
import com.harvestbackend.model.Farmer;
import com.harvestbackend.model.Product;
import com.harvestbackend.model.User;
import com.harvestbackend.payload.request.FarmerSignUpRequest;
import com.harvestbackend.payload.request.UpdateUserRequest;
import com.harvestbackend.repository.BidsRepository;
import com.harvestbackend.repository.UserRepository;
import com.harvestbackend.services.FarmerService;
import com.harvestbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    FarmerService farmerService;
    @Autowired
    BidsRepository bidsRepository;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/usertofarmer")
    public String UserToFarmerConverter(@RequestBody FarmerSignUpRequest farmerSignUpRequest){
        String  res = farmerService.userToFarmer(farmerSignUpRequest);
        return  res;
    }
    @PostMapping("/updateprofiledata")
    ResponseEntity<String > updateUserData(@RequestBody  UpdateUserRequest updateUserRequest){
        String  res = userService.updateUser(updateUserRequest);
        return ResponseEntity.ok(res);
    }
    @PostMapping("/updateprofilepic")
    ResponseEntity<String> updateUserProfilePic(@RequestParam(name = "profilePic") MultipartFile profilePic){
        String res = userService.updateProfilePic(profilePic);
        return ResponseEntity.ok(res);
    }
    @GetMapping("/getuser")
    ResponseEntity<User> getUser(){
        User user = userService.getUser();
        return ResponseEntity.ok(user);
    }
    @GetMapping("/getfarmer")
    ResponseEntity<Farmer> getfarmer(){
        Farmer farmer = userService.getFarmer();
        return ResponseEntity.ok(farmer);
    }
    @GetMapping("/getallfarmer")
    public ResponseEntity<List<Farmer>> getallfarmer(){
        List<Farmer> res = farmerService.getALL();
        return ResponseEntity.ok(res);
    }
    @GetMapping("/getproductoffarmer")
    public ResponseEntity<List<Product>> getAllProductOfFarmer(){
        List<Product> res = farmerService.getAllProductOfFarmer();
        return ResponseEntity.ok(res);

    }
    @GetMapping("/getbidoffarmer")
    public ResponseEntity<List<Bids>> getAllBidOfFarmer(){
        String  userDetails1 = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userDetails1).orElseThrow(() -> new RuntimeException("User not found"+ userDetails1));
        List<Bids> res = bidsRepository.findByUser(user);
        return ResponseEntity.ok(res);

    }
}
