package com.harvestbackend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harvestbackend.model.*;
import com.harvestbackend.payload.request.ProductRequest;
import com.harvestbackend.repository.FarmerRepository;
import com.harvestbackend.repository.ProductRepository;
import com.harvestbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    ProductRepository productRepository;
    @Autowired
    CloudinaryImageService cloudinaryImageService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    FarmerRepository farmerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BidsServiceImpl bidsServiceImpl;
    @Autowired
    EmailService emailService;
    @Autowired
    WalletService walletService;
    public  Product convertToProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
//        product.setType(EType.valueOf(productRequest.getType()));
        product.setWeight(productRequest.getWeight());
//        product.setWeightType(weightType.valueOf(productRequest.getWeightType()));
        product.setHarvestDate(productRequest.getHarvestDate());
        product.setExpiryDate(productRequest.getExpiryDate());
        product.setCity(productRequest.getCity());
        product.setState(productRequest.getState());
        product.setSeason(productRequest.getSeason());
        product.setTemperature(productRequest.getTemperature());
        product.setSoilType(productRequest.getSoilType());
        product.setStartingBid(productRequest.getStartingBid());
        product.setIncrement(productRequest.getIncrement());
        product.setTopBid(productRequest.getStartingBid());
        product.setDescription(productRequest.getDescription());
        return product;
    }

    public String weightTypeForType(String type) {
//        return switch (type) {
//            case "WHEAT", "CORN", "RICE" -> Wtype.TONNE.name();
//            default -> Wtype.KILOGRAM.name();
//        };
        return "KILOGRAM";
    }

    @Override
    public String saveProduct(String productRequest, MultipartFile thumbnail, MultipartFile displaypic1, MultipartFile displaypic2, MultipartFile displaypic3) throws IOException {
        Product product = null;
        EType type;
        Wtype WType ;

        try {
            ProductRequest productRequest1 = objectMapper.readValue(productRequest , ProductRequest.class);
            product = convertToProduct(productRequest1);
            String etype = productRequest1.getType().toUpperCase();
            String wttype = productRequest1.getWeightType().toUpperCase();
            switch (wttype){
                case "KILOGRAM":
                    WType = Wtype.KILOGRAM;
                    break;
                case "GRAM":
                    WType = Wtype.GRAM;
                    break;
                case "POUND":
                    WType = Wtype.POUND;
                    break;
                default:
                    WType = Wtype.TONNE;
                    break;
            }

            switch (etype){
                case "CORN":
                    type = EType.CORN;
                    break;
                case "SUGAR":
                    type = EType.SUGAR;
                    break;
                case "WHEAT":
                    type = EType.WHEAT;
                    break;
                case "RICE":
                    type = EType.RICE;
                    break;
                case "BANANAS":
                    type = EType.BANANAS;
                    break;
                case "ORANGES":
                    type = EType.ORANGES;
                    break;
                default:
                    type = EType.OTHER;;
                    break;
            }

        }catch (JsonProcessingException e){
            return "json problem !!";
        }
        String thumbnailurl ;
        String displaypic1url;
        String displaypic2url;
        String displaypic3url;
        try {
            Map map1;
            map1 = this.cloudinaryImageService.upload(thumbnail);
            thumbnailurl = (String) map1.get("url");
            map1 = this.cloudinaryImageService.upload(displaypic1);
            displaypic1url = (String) map1.get("url");
            map1 = this.cloudinaryImageService.upload(displaypic2);
            displaypic2url = (String) map1.get("url");
            map1 = this.cloudinaryImageService.upload(displaypic3);
            displaypic3url = (String) map1.get("url");


        }
        catch (IOException e){
            return "image uploadinf error !!";
        }
        product.setThumbnail(thumbnailurl);
        product.setDisplayPic1(displaypic1url);
        product.setDisplayPic2(displaypic2url);
        product.setDisplayPic3(displaypic3url);
        product.setType(type);
        product.setWtype(WType);
        String  userDetails1 = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userDetails1).orElseThrow(() -> new RuntimeException("User not found"+ userDetails1));
        if(user.getIsFarmer()){
            Product product1  =    productRepository.save(product);
            Farmer farmer = farmerRepository.findByUser(user);
            product1.setFarmer(farmer);
            product1.setPostDate(new Date());
            productRepository.save(product1);
            return "product are added ";
        }
        return "user  are  not able to add product ";
    }
    public Page<Product> searchByTypeAndNames(String type, String name, Pageable pageable) {
        EType type1 = convertToEType(type);
        return productRepository.findByTypeAndName(type1, name, pageable);
    }

    @Override
    public List<Product> searchByTypeAndName(String type, String name) {
        EType type1 = convertToEType(type);
        List<Product> res = productRepository.findByTypeAndName(type1 , name);
        return res;
    }



    private EType convertToEType(String type) {
        // Convert the type string to uppercase to match enum values
        String uppercaseType = type.toUpperCase();
        // Map the uppercase type string to the corresponding EType enum
        return switch (uppercaseType) {
            case "CORN" -> EType.CORN;
            case "SUGAR" -> EType.SUGAR;
            case "WHEAT" -> EType.WHEAT;
            case "RICE" -> EType.RICE;
            case "ORANGES" -> EType.ORANGES;
            case "BANANAS" -> EType.BANANAS;
            default -> EType.OTHER;
        };
    }

    public List<Product> getTopProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit); // Create a Pageable object with limit
        return productRepository.findAll(pageable).getContent(); // Retrieve the content of the first page
    }
    public List<Product> getTopProductsByLowestPrice(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("topBid"));
        return productRepository.findAll(pageable).getContent();
    }
    public List<Product> getAllProductByPageForm(int pageNumber , int pageSize){
        Pageable pageable = PageRequest.of(pageNumber , pageSize);
        return productRepository.findAll(pageable).getContent();
    }
    public String deleteProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(()->new RuntimeException("product are not found by id "+id));
        String  username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("user are not found by user name  "+username));
        Farmer farmer = farmerRepository.findByUser(user);
        if(!user.getIsFarmer()){
            return "user are not able to delete product and upload product !  ";
        }
        if (product.getFarmer().equals(farmer)){
           bidsServiceImpl.deletAllBidsRelatedThatProduct(product);
           productRepository.delete(product);
           return "farmer delete our product successfully !";
        }
        return "farmer are not able to delete other farmer product";
    }

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void endBid() {
        List<Product> products = productRepository.findAllByExpiryDateBefore(new Date());
        products.forEach(i -> {
            Bids bids = bidsServiceImpl.getTopBid(i.getId());
            if (bids != null) {
                User user = bids.getUser();
                if (!emailService.validateEmail(user.getEmail())) {
                    return;
                }
                Double price = (double) -bids.getPrice();
                walletService.updateMoneyForUser(user, price);
                String message = String.format("Your bid was successful for product %s with amount %d deducted from your account", i.getName(), bids.getPrice());
                emailService.sendMail("Your bid was successfully processed", user.getEmail(),message);
            }
        });
    }


//    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/webappimages";
//
//    @Override
//    public String saveProduct(String productRequest,  MultipartFile thumbnail ,  MultipartFile displaypic1 , MultipartFile displaypic2, MultipartFile displaypic3) throws IOException {
//        Product product = new Product();
//        product.setName(productRequest.getName());
//        product.setDescription(productRequest.getDescription());
//        product.setPostDate(new Date());
//
//        MultipartFile file1 = productRequest.getMultipartFiles1();
////        MultipartFile file2 = productRequest.getMultipartFiles2();
////        MultipartFile file3 = productRequest.getMultipartFiles3();
//        String file1Name = file1.getOriginalFilename();
////        String file2Name = file2.getOriginalFilename();
////        String file3Name = file3.getOriginalFilename();
//
//        UUID uuid = UUID.randomUUID();
//        file1Name = uuid+"-"+file1Name;
////         uuid = UUID.randomUUID();
////        file2Name = uuid+"-"+file2Name;
////        uuid = UUID.randomUUID();
////        file3Name = uuid+"-"+file3Name;
//
//        Path file1NameAndPath = Paths.get(uploadDirectory, file1Name);
//        Files.write(file1NameAndPath, file1.getBytes());
////        Path file2NameAndPath = Paths.get(uploadDirectory, file2Name);
////        Files.write(file2NameAndPath, file2.getBytes());
////        Path file3NameAndPath = Paths.get(uploadDirectory, file3Name);
////        Files.write(file3NameAndPath, file3.getBytes());
//        product.setThumbnail(file1Name);
////        product.setDisplayPic1(file2Name);
////        product.setDisplayPic2(file2Name);
//
//        productRepository.save(product);
//
//        return "data and file are added seccussfully";
//    }
}
