package com.kraken.orderbook.dto;

public class BidUpdate {

	private String price;
	private String volume;

	public BidUpdate(String price, String volume) {
		this.price = price;
		this.volume = volume;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

}
