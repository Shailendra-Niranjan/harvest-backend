package com.harvestbackend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class PricePredictionService {
    public static final Logger logger = LoggerFactory.getLogger(PricePredictionService.class);

    public Double getPrediction(Integer id) throws IOException {
        //add your python environment for 1st argcd c
        List<String> args = List.of("/home/shourya1/.conda/envs/myenv/bin/python", "price_prediction.py", String.valueOf(id));
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        //add the absolute path for the script directory
        processBuilder.directory(new File("/home/shourya1/IdeaProjects/harvest-backend/src/main/java/com/harvestbackend/scripts/"));
        Process process = processBuilder.start();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        int i = 0;
        while ((line = bufferedReader.readLine()) != null) {
            if (i++ == 3) return Double.parseDouble(line);
        }
        while ((line = errorStream.readLine()) != null) {
            logger.debug(line);
        }
        return 0.0;
    }
}
