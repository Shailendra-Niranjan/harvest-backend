package com.harvestbackend.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ProductRequest {

    private String name;
    private String type;

    private Integer weight;

    private String  weightType;

    private Date harvestdate;

    private Date expiryDate;

    private String city;

    private String state;
    private String season;
    private Integer temperature;

    private String soilType;

    private Integer startingBid;

    private String description;



}
