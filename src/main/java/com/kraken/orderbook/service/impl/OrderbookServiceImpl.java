package com.kraken.orderbook.service.impl;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.kraken.orderbook.handler.SocketHandler;
import com.kraken.orderbook.service.OrderbookService;

@Service("orderbookService")
public class OrderbookServiceImpl implements OrderbookService {
	
	@EventListener(ApplicationReadyEvent.class)
	public void testWebsocket() {
		WebSocketConnectionManager connectionManager = new WebSocketConnectionManager(
				new StandardWebSocketClient(), 
				new SocketHandler(), 
				"wss://ws.kraken.com");
		
		connectionManager.start();
	}

}
