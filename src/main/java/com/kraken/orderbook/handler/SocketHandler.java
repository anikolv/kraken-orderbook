package com.kraken.orderbook.handler;

import java.util.Arrays;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.orderbook.domain.OrderBook;
import com.kraken.orderbook.dto.OrderBookRequest;

public class SocketHandler extends TextWebSocketHandler {

	@Autowired
	private OrderBook orderBook;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		System.out.println("Message Received [" + message.getPayload() + "]");
		
		if (message.getPayload().contains("as") && message.getPayload().contains("bs")) {
			// snapshot with full book
			JSONArray jsonarray = new JSONArray(message.getPayload());
			JSONObject snapshotContainer = (JSONObject) jsonarray.get(1);
			JSONArray asksContainer = snapshotContainer.getJSONArray("as");
			
			Iterator<Object> asksIterator = asksContainer.iterator();
			while (asksIterator.hasNext()) {
				Object askPrice = asksIterator.next();
				System.out.println();
			}
			JSONArray test = asksContainer.getJSONArray(0);
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("Connected");

		OrderBookRequest orderBookRequest = new OrderBookRequest("subscribe", "book", Arrays.asList("ETC/USD"));
		String payload = objectMapper.writeValueAsString(orderBookRequest);

		System.out.println("Sending [" + payload + "]");

		session.sendMessage(new TextMessage(payload));
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) {
		System.out.println("Transport Error");
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		System.out.println("Connection Closed [" + status.getReason() + "]");
	}
}