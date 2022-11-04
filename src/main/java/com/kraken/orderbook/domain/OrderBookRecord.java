package com.kraken.orderbook.domain;

public class OrderBookRecord implements Comparable<OrderBookRecord> {

	private Double price;
	private Long volume;

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
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
