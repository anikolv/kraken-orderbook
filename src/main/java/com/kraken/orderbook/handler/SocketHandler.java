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
import com.kraken.orderbook.service.KrakenOrderBookService;

public class SocketHandler extends TextWebSocketHandler {

	private KrakenOrderBookService orderBookService = new KrakenOrderBookService();
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		// orderbook snapshot event
		if (isSnapshotEvent(message)) {
			handleOrderbookSnapshotEvent(message);
		}
		// orderbook ask update event
		if (isAskUpdateEvent(message) && !isSnapshotEvent(message) && !isSystemEvent(message)) {
			handleAskUpdateEvent(message);
		}
		// orderbook bid update event
		if (isBidUpdateEvent(message) && !isSnapshotEvent(message) && !isSystemEvent(message)) {
			handleBidUpdateEvent(message);
		}
		orderBookService.printOrderBook();
	}

	private boolean isSnapshotEvent(TextMessage message) {
		boolean isSnapshotEvent = message.getPayload().contains("as") && message.getPayload().contains("bs");
		return isSnapshotEvent;
	}

	private boolean isAskUpdateEvent(TextMessage message) {
		boolean isAskUpdateEvent = message.getPayload().contains("\"a\"");
		return isAskUpdateEvent;
	}

	private boolean isBidUpdateEvent(TextMessage message) {
		boolean isBidUpdateEvent = message.getPayload().contains("\"b\"");
		return isBidUpdateEvent;
	}

	private boolean isSystemEvent(TextMessage message) {
		boolean isConnectionStatusEvent = message.getPayload().contains("connectionID");
		boolean isSubscriptionStatusEvent = message.getPayload().contains("subscriptionStatus");
		boolean isHeartBeatEvent = message.getPayload().contains("heartbeat");
		return isConnectionStatusEvent || isSubscriptionStatusEvent || isHeartBeatEvent;
	}

	private void handleOrderbookSnapshotEvent(TextMessage message) {
		try {
			// fetch snapshot data
			JSONArray jsonarray = new JSONArray(message.getPayload());
			String pair = (String) jsonarray.get(3);
			JSONObject snapshotContainer = (JSONObject) jsonarray.get(1);

			// process asks
			JSONArray asksContainer = snapshotContainer.getJSONArray("as");
			Iterator<Object> asksIterator = asksContainer.iterator();
			while (asksIterator.hasNext()) {
				JSONArray ask = (JSONArray) asksIterator.next();
				orderBookService.handleSnapshotEvent(pair,
						new OrderBookRecord(Double.valueOf((String) ask.get(0)), Double.valueOf((String) ask.get(1))),
						true);
			}

			// process bids
			JSONArray bidsContainer = snapshotContainer.getJSONArray("bs");
			Iterator<Object> bidsIterator = bidsContainer.iterator();
			while (bidsIterator.hasNext()) {
				JSONArray bid = (JSONArray) bidsIterator.next();
				orderBookService.handleSnapshotEvent(pair,
						new OrderBookRecord(Double.valueOf((String) bid.get(0)), Double.valueOf((String) bid.get(1))),
						false);
			}
		} catch (Exception e) {
			System.out.println("An error occured while handling snapshot event from server: " + e.getMessage());
		}
	}

	private void handleBidUpdateEvent(TextMessage message) {
		try {
			// fetch update data
			JSONArray jsonarray = new JSONArray(message.getPayload());
			String pair = (String) jsonarray.get(3);
			JSONObject updateDataContainer = (JSONObject) jsonarray.get(1);

			// fetch bids
			JSONArray bidsContainer = updateDataContainer.getJSONArray("b");
			Iterator<Object> bidsIterator = bidsContainer.iterator();
			while (bidsIterator.hasNext()) {
				JSONArray bid = (JSONArray) bidsIterator.next();
				orderBookService.handleUpdateEvent(pair,
						new OrderBookRecord(Double.valueOf((String) bid.get(0)), Double.valueOf((String) bid.get(1))),
						false);
			}
		} catch (Exception e) {
			System.out.println("An error occured while handling bid update event from server: " + e.getMessage());
		}
	}

	private void handleAskUpdateEvent(TextMessage message) {
		try {
			// fetch update data
			JSONArray jsonarray = new JSONArray(message.getPayload());
			String pair = (String) jsonarray.get(3);
			JSONObject updateDataContainer = (JSONObject) jsonarray.get(1);

			// fetch asks
			JSONArray asksContainer = updateDataContainer.getJSONArray("a");
			Iterator<Object> asksIterator = asksContainer.iterator();
			while (asksIterator.hasNext()) {
				JSONArray ask = (JSONArray) asksIterator.next();
				orderBookService.handleUpdateEvent(pair,
						new OrderBookRecord(Double.valueOf((String) ask.get(0)), Double.valueOf((String) ask.get(1))),
						true);
			}
		} catch (Exception e) {
			System.out.println("An error occured while handling ask update event from server: " + e.getMessage());
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("Connected");

		String requestPayload = objectMapper
				.writeValueAsString(new OrderBookRequest("subscribe", "book", Arrays.asList("BTC/USD", "ETC/USD")));

		System.out.println("Sending [" + requestPayload + "]");

		session.sendMessage(new TextMessage(requestPayload));
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