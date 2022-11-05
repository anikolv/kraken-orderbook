package com.kraken.orderbook.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KrakenOrderBook {

	private String currencyPair;
	private List<OrderBookRecord> asks = new ArrayList<>();
	private List<OrderBookRecord> bids = new ArrayList<>();
	
	public KrakenOrderBook(String currencyPair) {
		this.currencyPair = currencyPair;
	}

	public String getCurrencyPair() {
		return currencyPair;
	}

	public void setCurrencyPair(String currencyPair) {
		this.currencyPair = currencyPair;
	}

	public List<OrderBookRecord> getAsks() {
		return asks;
	}

	public void addAsk(OrderBookRecord ask) {
		this.asks.add(ask);
	}

	public List<OrderBookRecord> getBids() {
		return bids;
	}

	public void addBid(OrderBookRecord bid) {
		this.bids.add(bid);
	}
	
	public void sortAsks() {
		Collections.sort(asks);
		Collections.reverse(asks);
	}
	
	public void sortBids() {
		Collections.sort(bids);
		Collections.reverse(bids);
	}
}
