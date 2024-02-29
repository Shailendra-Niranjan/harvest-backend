package com.harvestbackend.controller;

import com.harvestbackend.services.CloudinaryImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/cloud/upload")
public class CloudinaryImageUploadController {
    @Autowired
    private CloudinaryImageService cloudinaryImageService;

    @PostMapping("/save")
    public ResponseEntity<Map> uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
        Map upload = this.cloudinaryImageService.upload(file);
        return ResponseEntity.ok(upload);
    }
    @GetMapping("/showproduct")
    public String showmes(){
        return "this is product";
    }
    @GetMapping("/destory")
    public ResponseEntity<Map> destoryImage(@RequestParam("public_id")String public_id) throws IOException {
        Map upload = this.cloudinaryImageService.deleteImg(public_id);
        return ResponseEntity.ok(upload);
    }


}
