package com.harvestbackend.services;

import com.harvestbackend.model.Farmer;
import com.harvestbackend.model.User;
import com.harvestbackend.payload.request.UpdateUserRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserService {

    String updateUser(UpdateUserRequest updateUserRequest);
    String updateProfilePic(MultipartFile profilePic);
    User getUser();
    Farmer getFarmer();

}
