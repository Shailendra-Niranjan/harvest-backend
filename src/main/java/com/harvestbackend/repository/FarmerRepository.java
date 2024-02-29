package com.harvestbackend.repository;

import com.harvestbackend.model.Farmer;
import com.harvestbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    Farmer findByUser(User user);

}
