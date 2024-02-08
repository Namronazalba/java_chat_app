package com.example.chat.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import java.util.UUID;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(
           @Payload ChatMessage chatMessage
    ){
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ){
        String username = chatMessage.getSender();
        String sessionId = generateSessionId();
        headerAccessor.getSessionAttributes().put("username", username);
        headerAccessor.getSessionAttributes().put("sessionId", sessionId);

        ChatMessage joinMessage = ChatMessage.builder()
                .type(MessageType.JOIN)
                .sender(chatMessage.getSender())
                .sessionId(sessionId)
                .build();

        return joinMessage;
    }


    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
