package com.harvestbackend.controller;

import com.harvestbackend.payload.request.ChatMessage;
import com.harvestbackend.payload.response.MessageResponse;
import com.harvestbackend.services.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/chatbot")
@CrossOrigin(value = "*")
public class ChatbotController {

    @Autowired
    ChatbotService chatbotService;

    @PostMapping("/response")
    public ResponseEntity<?> getResponse(@RequestBody List<ChatMessage> chatMessages, @RequestParam(name = "name") String name) throws IOException {
        String message = chatbotService.getResponse(chatMessages, name);
        return ResponseEntity.ok(new MessageResponse(message));
    }

}
