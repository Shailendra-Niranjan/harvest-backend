package com.harvestbackend.controller;

import com.harvestbackend.services.PricePredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/predict")
@CrossOrigin(value = "*")
public class PricePredictionController {

    @Autowired
    PricePredictionService pricePredictionService;

    @GetMapping("/{type}")
    public Double getPrediction(@PathVariable String type) throws IOException {
        return switch (type) {
            case "sugar" -> pricePredictionService.getPrediction(0);
            case "bananas" -> pricePredictionService.getPrediction(1);
            case "corn" -> pricePredictionService.getPrediction(2)/1000;
            case "oranges" -> pricePredictionService.getPrediction(3);
            case "wheat" -> pricePredictionService.getPrediction(4)/1000;
            default -> pricePredictionService.getPrediction(5)/1000;
        };
    }
}
