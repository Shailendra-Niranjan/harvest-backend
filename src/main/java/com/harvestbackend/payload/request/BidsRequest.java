package com.harvestbackend.payload.request;

import com.harvestbackend.model.Product;
import com.harvestbackend.model.User;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidsRequest {
    private Integer price;

    private Product product;
}
