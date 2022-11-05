package com.kraken.orderbook.domain;

public class OrderBookRecord implements Comparable<OrderBookRecord> {

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

	@Override
	public int compareTo(OrderBookRecord inboundRecord) {
		if (this.price < inboundRecord.getPrice())
			return -1;
		else if (inboundRecord.getPrice() < this.price)
			return 1;
		return 0;
	}

}
