package com.harvestbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private EType type;
    private Integer weight;
    @Enumerated(EnumType.STRING)
    private Wtype Wtype;
    private Date harvestdate;
    private Date postDate;
    private Date expiryDate;
    private String city;
    private String state;
    private String season;
    private Integer temperature;
    private String soilType;
    private Integer startingBid;
    private Integer topBid;
    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    private String description;
    @Lob
    private String  thumbnail;
    @Lob
    private String  displayPic1;
    @Lob
    private String  displayPic2;
    @Lob
    private String  displayPic3;


    public Product(String name, EType type, Integer weight, Wtype Wtype, Date date, Date postDate, Date expiryDate, String city, String state, String season, Integer temperature, String soilType, Integer startingBid, Integer topBid,  String description) {
        this.name = name;
        this.type = type;
        this.weight = weight;
        this.Wtype = Wtype;
        this.harvestdate = date;
        this.postDate = postDate;
        this.expiryDate = expiryDate;
        this.city = city;
        this.state = state;
        this.season = season;
        this.temperature = temperature;
        this.soilType = soilType;
        this.startingBid = startingBid;
        this.topBid = topBid;

        this.description = description;

    }
}
