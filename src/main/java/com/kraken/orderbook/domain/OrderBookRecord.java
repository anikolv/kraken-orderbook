package com.kraken.orderbook.domain;

public class OrderBookRecord {

	private Double price;
	private Double volume;
	
	public OrderBookRecord(Double price, Double volume) {
		this.price = price;
		this.volume = volume;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}
}
