package com.kraken.orderbook.dto;

import java.util.List;

public class OrderBookRequest {

	private String event;
	private Subscription subscription;
	private List<String> pair;

	public OrderBookRequest(String event, String subscription, List<String> pair) {
		this.event = event;
		this.subscription = new Subscription(subscription);
		this.pair = pair;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public List<String> getPair() {
		return pair;
	}

	public void setPair(List<String> pair) {
		this.pair = pair;
	}

}
