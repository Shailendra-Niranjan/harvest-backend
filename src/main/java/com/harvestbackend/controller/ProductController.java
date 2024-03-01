package com.harvestbackend.controller;

import com.harvestbackend.model.Product;
import com.harvestbackend.payload.request.FarmerSignUpRequest;
import com.harvestbackend.payload.request.ProductRequest;
import com.harvestbackend.repository.ProductRepository;
import com.harvestbackend.services.FarmerService;
import com.harvestbackend.services.ProductService;
import com.harvestbackend.services.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    FarmerService farmerService;
    @Autowired
    ProductServiceImpl productServiceimpl;
    @Autowired
    ProductRepository productRepository;
    @GetMapping("/products")
    public List<Product> getAllProducts(@RequestParam(name = "pageNumber" , defaultValue = "0") int pageNumber , @RequestParam(name = "pageSize" , defaultValue = "10") int pageSize) {
        return productServiceimpl.getAllProductByPageForm(pageNumber , pageSize);
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }


    @PostMapping("/save")
    public String saveProducts(@RequestParam(name = "ProductRequest")  String productRequest, @RequestParam(name = "thumbnail")MultipartFile thumbnail , @RequestParam("displaypic1") MultipartFile displaypic1 ,@RequestParam("displaypic2") MultipartFile displaypic2,@RequestParam("displaypic3") MultipartFile displaypic3) {
        try {
            return productService.saveProduct(productRequest , thumbnail , displaypic1 , displaypic2 , displaypic3);
        } catch (IOException e) {

            return "Error saving product: " + e.getMessage();
        }
    }
    @GetMapping("/searchproduct")
    ResponseEntity<List<Product>> searchProduct(@RequestParam(name = "type") String type , @RequestParam(name = "name") String pname){
        List<Product> res = productService.searchByTypeAndName(type ,pname);
        return ResponseEntity.ok(res);
    }
    @GetMapping("/sreacrhproductspagging")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "3") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> products = productServiceimpl.searchByTypeAndNames(type, name,pageable);
        return ResponseEntity.ok(products.getContent());
    }


    @GetMapping("/top-products")
    public List<Product> getTopProducts(@RequestParam(name = "limit", defaultValue = "6") Integer limit) {
        return productServiceimpl.getTopProducts(limit);
    }

    @GetMapping("/cheap-product")
    public List<Product> getTopProductsByLowestPrice(@RequestParam(name = "limit", defaultValue = "6") int limit) {
        return productServiceimpl.getTopProductsByLowestPrice(limit);
    }
    @GetMapping("/delete-product")
    public ResponseEntity<String > deleteProduct(@RequestParam(name = "productId") Long productId){
        String  res = productServiceimpl.deleteProduct(productId);
        return ResponseEntity.ok(res);
    }

}
