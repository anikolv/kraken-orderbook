package com.kraken.orderbook.handler;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.kraken.orderbook.dto.OrderBookDto;

public class KrakenStompSessionHandler extends StompSessionHandlerAdapter {


	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		System.out.println("New session established : " + session.getSessionId());
		session.subscribe("/operations", this);
		System.out.println("Subscribed to /operations");
	}

	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
			Throwable exception) {
		System.out.println(exception.getMessage());
	}

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return OrderBookDto.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		OrderBookDto ops = (OrderBookDto) payload;
		System.out.println(ops);
	}
}