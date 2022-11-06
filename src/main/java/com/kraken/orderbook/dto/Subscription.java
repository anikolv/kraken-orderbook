package com.kraken.orderbook.dto;

public class Subscription {

	private String name;

	public Subscription(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
