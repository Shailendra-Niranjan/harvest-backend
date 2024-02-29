package com.harvestbackend.services;

import com.harvestbackend.model.Farmer;
import com.harvestbackend.model.User;
import com.harvestbackend.payload.request.UpdateUserRequest;
import com.harvestbackend.repository.FarmerRepository;
import com.harvestbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CloudinaryImageService cloudinaryImageService;
    @Autowired
    FarmerRepository farmerRepository;
    @Override
    public String updateUser(UpdateUserRequest updateUserRequest) {
        String  userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName).orElseThrow(()->new RuntimeException("user not found "+ userName));
        if(userRepository.existsByEmail(updateUserRequest.getEmail())){
            return "email already exist please change it";
        }
        user.setFirstName(updateUserRequest.getFirstName());
        user.setLastName(updateUserRequest.getLastName());
        user.setAddress(updateUserRequest.getAddress());
        user.setEmail(updateUserRequest.getEmail());
        userRepository.save(user);

        return "update successfully !!";
    }

    @Override
    public String updateProfilePic(MultipartFile profilePic) {
        String  userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName).orElseThrow(()->new RuntimeException("user not found "+ userName));
        String updatePic = null;
        try{
            Map map1;
            map1 = this.cloudinaryImageService.upload(profilePic);
            updatePic = (String) map1.get("url");
        }
        catch (IOException e){
            return "image upload error !!";
        }
        user.setProfileImage(updatePic);
        userRepository.save(user);
        return updatePic;
    }

    @Override
    public User getUser() {
        String  userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName).orElseThrow(()->new RuntimeException("user not found "+ userName));
        return user;
    }

    @Override
    public Farmer getFarmer() {
        String  userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName).orElseThrow(()->new RuntimeException("user not found "+ userName));
        Farmer farmer = farmerRepository.findByUser(user);
        return farmer;
    }
}
