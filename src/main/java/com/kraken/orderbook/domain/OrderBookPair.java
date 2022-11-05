package com.kraken.orderbook.domain;

import java.util.Objects;

public class OrderBookPair {

	private String pair;

	public OrderBookPair(String pair) {
		this.pair = pair;
	}

	public String getPair() {
		return pair;
	}

	public void setPair(String pair) {
		this.pair = pair;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pair);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderBookPair other = (OrderBookPair) obj;
		return Objects.equals(pair, other.pair);
	}
}
