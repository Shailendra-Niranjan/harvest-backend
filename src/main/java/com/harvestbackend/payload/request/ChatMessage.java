package com.harvestbackend.payload.request;

import lombok.Getter;

@Getter
public class ChatMessage {
    String role;
    String content;

    @Override
    public String toString() {
        return String.format("{ 'role': '%s', 'content': '%s' }", role, content);
    }
}
