package com.kraken.orderbook.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterables;
import com.kraken.orderbook.domain.KrakenOrderBook;
import com.kraken.orderbook.domain.OrderBookRecord;

public class OrderBookService {

	private List<KrakenOrderBook> orderBooks = new ArrayList<>();

	public void handleAskUpdateEvent(String pair, OrderBookRecord updateData) {
		for (KrakenOrderBook orderBook : orderBooks) {
			if (orderBook.getCurrencyPair().equals(pair)) {
				// handle delete event
				if (updateData.getVolume().equals(0.0d)) {
					orderBook.getAsks().removeIf(ask -> ask.getPrice().equals(updateData.getPrice()));
					return;
				}

				// handle insert event
				boolean isNewAsk = orderBook.getAsks().stream()
						.noneMatch(ask -> ask.getPrice().equals(updateData.getPrice()));
				if (isNewAsk) {
					orderBook.addAsk(updateData);
					return;
				}

				// handle update event
				for (OrderBookRecord ask : orderBook.getAsks()) {
					if (ask.getPrice().equals(updateData.getPrice())) {
						ask.setVolume(updateData.getVolume());
					}
				}
			}
			orderBook.sortAsks();
		}
	}

	public void handleBidUpdateEvent(String pair, OrderBookRecord updateData) {
		for (KrakenOrderBook orderBook : orderBooks) {
			if (orderBook.getCurrencyPair().equals(pair)) {
				// handle delete event
				if (updateData.getVolume().equals(0.0d)) {
					orderBook.getBids().removeIf(bid -> bid.getPrice().equals(updateData.getPrice()));
					return;
				}

				// handle insert event
				if (orderBook.getBids().stream().noneMatch(bid -> bid.getPrice().equals(updateData.getPrice()))) {
					orderBook.addBid(updateData);
					return;
				}

				// handle update event
				for (OrderBookRecord bid : orderBook.getBids()) {
					if (bid.getPrice().equals(updateData.getPrice())) {
						bid.setVolume(updateData.getVolume());
					}
				}
			}
			orderBook.sortBids();
		}
	}

	public void handleAskSnapshotEvent(String pair, OrderBookRecord record) {
		if (orderBooks.stream().anyMatch(b -> b.getCurrencyPair().equals(pair))) {
			for (KrakenOrderBook krakenOrderBook : orderBooks) {
				if (krakenOrderBook.getCurrencyPair().equals(pair)) {
					krakenOrderBook.addAsk(record);
				}
			}
		} else {
			KrakenOrderBook orderBook = new KrakenOrderBook(pair);
			orderBook.addAsk(record);
			orderBooks.add(orderBook);
		}
	}

	public void handleBidSnapshotEvent(String pair, OrderBookRecord record) {
		if (orderBooks.isEmpty()) {
			KrakenOrderBook orderBook = new KrakenOrderBook(pair);
			orderBook.addBid(record);
			orderBooks.add(orderBook);
		} else {
			for (KrakenOrderBook krakenOrderBook : orderBooks) {
				if (krakenOrderBook.getCurrencyPair().equals(pair)) {
					krakenOrderBook.addBid(record);
				}
			}
		}
	}

	public void printOrderBook() {
		Iterator<KrakenOrderBook> orderBookIterator = orderBooks.iterator();

		while (orderBookIterator.hasNext()) {
			KrakenOrderBook pairOrderBook = orderBookIterator.next();

			System.out.println("asks:");
			System.out.print("[ ");

			Iterator<OrderBookRecord> asksIterator = pairOrderBook.getAsks().iterator();
			while (asksIterator.hasNext()) {
				OrderBookRecord ask = asksIterator.next();
				System.out.print("[ " + ask.getPrice() + ", " + ask.getVolume() + " ]");
				if (asksIterator.hasNext()) {
					System.out.println(",");
				}
			}
			System.out.println(" ]");

			OrderBookRecord bestBid = pairOrderBook.getBids().stream().findFirst().get();
			System.out.println("best bid: [ " + bestBid.getPrice() + ", " + bestBid.getVolume() + " ]");

			OrderBookRecord bestAsk = Iterables.getLast(pairOrderBook.getAsks());
			System.out.println("best ask: [ " + bestAsk.getPrice() + ", " + bestAsk.getVolume() + " ]");

			System.out.println("bids:");
			System.out.print("[ ");

			Iterator<OrderBookRecord> bidsIterator = pairOrderBook.getBids().iterator();
			while (bidsIterator.hasNext()) {
				OrderBookRecord bid = bidsIterator.next();
				System.out.print("[ " + bid.getPrice() + ", " + bid.getVolume() + " ]");
				if (bidsIterator.hasNext()) {
					System.out.println(",");
				}
			}
			System.out.println(" ]");
			System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
			System.out.println(pairOrderBook.getCurrencyPair());

			System.out.println(">-------------------------------------<");
			System.out.println("<------------------------------------->");
		}
	}
}
