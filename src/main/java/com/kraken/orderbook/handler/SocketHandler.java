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
		boolean isSnapshotEvent = message.getPayload().contains("as") && message.getPayload().contains("bs");
		boolean isAskUpdateEvent = message.getPayload().contains("\"a\"");
		boolean isBidUpdateEvent = message.getPayload().contains("\"b\"");

		// orderbook snapshot event
		if (isSnapshotEvent) {
			handleOrderbookSnapshotEvent(message);
		}

		// orderbook ask update event
		if (!isSnapshotEvent && !isSystemEvent(message) && isAskUpdateEvent) {
			handleAskUpdateEvent(message);
		}

		// orderbook bid update event
		if (!isSnapshotEvent && !isSystemEvent(message) && isBidUpdateEvent) {
			handleBidUpdateEvent(message);
		}

		orderBookService.printOrderBook();
	}
	
	private void handleOrderbookSnapshotEvent(TextMessage message) {
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
					new OrderBookRecord(Double.valueOf((String) ask.get(0)), Double.valueOf((String) ask.get(1))), true);
		}

		// process bids
		JSONArray bidsContainer = snapshotContainer.getJSONArray("bs");
		Iterator<Object> bidsIterator = bidsContainer.iterator();
		while (bidsIterator.hasNext()) {
			JSONArray bid = (JSONArray) bidsIterator.next();
			orderBookService.handleSnapshotEvent(pair,
					new OrderBookRecord(Double.valueOf((String) bid.get(0)), Double.valueOf((String) bid.get(1))), false);
		}
	}
	
	private void handleBidUpdateEvent(TextMessage message) {
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
	}

	private void handleAskUpdateEvent(TextMessage message) {
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
	}

	private boolean isSystemEvent(TextMessage message) {
		boolean isConnectionStatusEvent = message.getPayload().contains("connectionID");
		boolean isSubscriptionStatusEvent = message.getPayload().contains("subscriptionStatus");
		boolean isHeartBeatEvent = message.getPayload().contains("heartbeat");

		return isConnectionStatusEvent || isSubscriptionStatusEvent || isHeartBeatEvent;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("Connected");

		OrderBookRequest orderBookRequest = new OrderBookRequest("subscribe", "book",
				Arrays.asList("BTC/USD", "ETC/USD"));
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