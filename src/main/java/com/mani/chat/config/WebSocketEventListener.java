package com.mani.chat.config;

import com.mani.chat.dto.ChatMessage;
import com.mani.chat.dto.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j

public class WebSocketEventListener  {
    private final SimpMessageSendingOperations sendingOperations;
    
     @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
         StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
         String userName = (String) headerAccessor.getSessionAttributes().get("username");
         if(userName !=null){
             log.info("User disconnected: {}",userName);
             var chatMessage = ChatMessage.builder()
                     .type(MessageType.LEAVE)
                     .sender(userName)
                     .build();
             sendingOperations.convertAndSend("/topic/public",chatMessage);
         }
    }
}
