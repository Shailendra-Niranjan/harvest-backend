package com.harvestbackend.services;

import com.harvestbackend.model.EType;
import com.harvestbackend.model.Product;
import com.harvestbackend.payload.request.ProductRequest;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    String saveProduct(String productRequest,  MultipartFile thumbnail ,  MultipartFile displaypic1 , MultipartFile displaypic2, MultipartFile displaypic3) throws IOException;
    List<Product> searchByTypeAndName(String  type, String name);
}
