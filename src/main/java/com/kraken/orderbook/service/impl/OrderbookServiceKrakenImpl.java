package com.kraken.orderbook.service.impl;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.kraken.orderbook.handler.KrakenStompSessionHandler;
import com.kraken.orderbook.service.OrderbookService;

@Service("orderbookService")
public class OrderbookServiceKrakenImpl implements OrderbookService {
	
	@Autowired
	private WebSocketStompClient stompClient;
	
	@EventListener(ApplicationReadyEvent.class)
	public void testWebsocket() {        
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler sessionHandler = new KrakenStompSessionHandler();
        
        String url = "wss://ws.kraken.com";
        stompClient.connect(url, sessionHandler);
	}
}
