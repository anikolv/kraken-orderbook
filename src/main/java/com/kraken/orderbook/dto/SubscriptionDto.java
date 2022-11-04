package com.kraken.orderbook.dto;

public class SubscriptionDto {

	private String name;

	public SubscriptionDto(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
