package com.aditya.charapp.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.aditya.charapp.model.ChatMessage;

public class WebSocketEventListener {
	
	private static final Logger logger  =  LoggerFactory.getLogger(WebSocketEventListener.class);
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	@EventListener
	public void handleWebSocketConnectionListener(SessionConnectedEvent event) {
		logger.info("Received a new web socket connection");
	}
	
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		
		String username = (String) headerAccessor.getSessionAttributes().get("username");
		
		if(username!=null) {
			logger.info("User Disonnected : "+username);
			ChatMessage message = new ChatMessage();
			message.setType(ChatMessage.MessageType.LEAVE);
			message.setSender(username);
			
			messagingTemplate.convertAndSend("/topic/public", message);
		}
	}

}
