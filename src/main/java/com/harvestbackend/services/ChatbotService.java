package com.harvestbackend.services;

import com.harvestbackend.payload.request.ChatMessage;
import com.harvestbackend.payload.response.MessageResponse;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatbotService {

    public static final Logger logger = LoggerFactory.getLogger(ChatbotService.class);

    public String getResponse(List<ChatMessage> chatMessages, String name)  throws IOException {
        //add your python environment for 1st arg
        List<String> args = List.of("/home/shourya1/.conda/envs/myenv/bin/python", "chatbot.py", "--messages", String.format("%s", chatMessages.toString()), "--name",  String.format("%s", name));
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        //add the absolute path for script directory
        processBuilder.directory(new File("/home/shourya1/IdeaProjects/harvest-backend/src/main/java/com/harvestbackend/scripts/"));
        Process process = processBuilder.start();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        StringBuilder message = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            message.append(line);
            message.append("<br/>");
        }
        while ((line = errorStream.readLine()) != null) {
            logger.debug(line);
        }
        return message.toString();
    }
}
