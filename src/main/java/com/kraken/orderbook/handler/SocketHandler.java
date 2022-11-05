package com.kraken.orderbook.handler;

import java.util.Arrays;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.orderbook.domain.OrderBookRecord;
import com.kraken.orderbook.dto.OrderBookRequest;
import com.kraken.orderbook.service.OrderBookService;

public class SocketHandler extends TextWebSocketHandler {

	private OrderBookService orderBookService = new OrderBookService();
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
//		System.out.println("Message Received [" + message.getPayload() + "]");

		if (message.getPayload().contains("as") && message.getPayload().contains("bs")) {
			JSONArray jsonarray = new JSONArray(message.getPayload());
			String pair = (String) jsonarray.get(3);
			JSONObject snapshotContainer = (JSONObject) jsonarray.get(1);

			// fetch asks
			JSONArray asksContainer = snapshotContainer.getJSONArray("as");

			Iterator<Object> asksIterator = asksContainer.iterator();
			while (asksIterator.hasNext()) {
				JSONArray askPrice = (JSONArray) asksIterator.next();
				String price = (String) askPrice.get(0);
				String volume = (String) askPrice.get(1);

				orderBookService.addAskRecord(pair, new OrderBookRecord(Double.valueOf(price), Double.valueOf(volume)));
			}

			// fetch bids
			JSONArray bidsContainer = snapshotContainer.getJSONArray("bs");

			Iterator<Object> bidsIterator = bidsContainer.iterator();
			while (bidsIterator.hasNext()) {
				JSONArray askPrice = (JSONArray) bidsIterator.next();
				String price = (String) askPrice.get(0);
				String volume = (String) askPrice.get(1);

				orderBookService.addBidsRecord(pair,
						new OrderBookRecord(Double.valueOf(price), Double.valueOf(volume)));
			}
			orderBookService.printOrderBook();
		}

	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("Connected");

		OrderBookRequest orderBookRequest = new OrderBookRequest("subscribe", "book", Arrays.asList("BTC/USD", "ETC/USD"));
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