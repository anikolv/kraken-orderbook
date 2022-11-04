package com.kraken.orderbook.handler;

import java.util.Arrays;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.orderbook.dto.OrderBookRequest;

public class SocketHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("Message Received [" + message.getPayload() + "]");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	System.out.println("Connected");

        OrderBookRequest orderBookRequest = 
        		new OrderBookRequest("subscribe", "book", Arrays.asList("ETC/USD"));
        String payload = new ObjectMapper().writeValueAsString(orderBookRequest);
        
        System.out.println("Sending [" + payload + "]");
        
        session.sendMessage(new TextMessage(payload));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
    	System.out.println("Transport Error");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
    	System.out.println("Connection Closed [" + status.getReason() + "]");
    }
}