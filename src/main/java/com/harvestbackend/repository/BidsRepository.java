package com.harvestbackend.repository;

import com.harvestbackend.model.Bids;
import com.harvestbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BidsRepository extends JpaRepository<Bids , Long> {

    List<Bids> findByUser(User user);
}
