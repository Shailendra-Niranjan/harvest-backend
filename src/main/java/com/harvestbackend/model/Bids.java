package com.harvestbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
public class Bids {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer price;
    private Date date;
    @ManyToOne
    private User user;
    @ManyToOne
    private Product product;
    @ManyToOne
    private Farmer farmer;


}
