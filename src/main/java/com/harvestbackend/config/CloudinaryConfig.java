package com.harvestbackend.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.ModelMap;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {


    @Bean
    public Cloudinary  getCloudinary(){
        Map map = new HashMap<>();
        map.put("cloud_name" ,"dzu0bryd5");
        map.put("api_key" ,"388697614347676");
        map.put("api_secret" ,"l3h-_qOwtOao0383U-StT68GN-M");
        map.put("secure" , true);

        return new Cloudinary(map);

    }
}
