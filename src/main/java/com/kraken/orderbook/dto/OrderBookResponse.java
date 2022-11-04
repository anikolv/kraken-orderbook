package com.kraken.orderbook.dto;

import java.util.ArrayList;

public class OrderBookResponse {

    private ArrayList<Object> bookUpdates;

	public ArrayList<Object> getBookUpdates() {
		return bookUpdates;
	}

	public void setBookUpdates(ArrayList<Object> bookUpdates) {
		this.bookUpdates = bookUpdates;
	}
}
