package com.kraken.orderbook.dto;

import java.util.List;

public class OrderBookRequest {

	private String event;
	private SubscriptionDto subscription;
	private List<String> pair;

	public OrderBookRequest(String event, String subscription, List<String> pair) {
		this.event = event;
		this.subscription = new SubscriptionDto(subscription);
		this.pair = pair;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public SubscriptionDto getSubscription() {
		return subscription;
	}

	public void setSubscription(SubscriptionDto subscription) {
		this.subscription = subscription;
	}

	public List<String> getPair() {
		return pair;
	}

	public void setPair(List<String> pair) {
		this.pair = pair;
	}

}
