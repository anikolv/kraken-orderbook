package com.kraken.orderbook.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterables;
import com.kraken.orderbook.domain.KrakenOrderBook;
import com.kraken.orderbook.domain.OrderBookRecord;

public class KrakenOrderBookService {

	private List<KrakenOrderBook> orderBooks = new ArrayList<>();

	public void handleUpdateEvent(String pair, OrderBookRecord updateData, boolean isAsk) {
		for (KrakenOrderBook orderBook : orderBooks) {
			if (orderBook.getCurrencyPair().equals(pair)) {

				// fetch the proper order book
				List<OrderBookRecord> records = isAsk ? orderBook.getAsks() : orderBook.getBids();

				// handle delete event
				if (updateData.getVolume().equals(0.0d)) {
					records.removeIf(record -> record.getPrice().equals(updateData.getPrice()));
					return;
				}

				// handle insert event
				boolean isNewRecord = records.stream()
						.noneMatch(record -> record.getPrice().equals(updateData.getPrice()));
				if (isNewRecord) {
					if (isAsk) {
						orderBook.addAsk(updateData);
					} else {
						orderBook.addBid(updateData);
					}
					return;
				}

				// handle update event
				for (OrderBookRecord record : records) {
					if (record.getPrice().equals(updateData.getPrice())) {
						record.setVolume(updateData.getVolume());
					}
				}
			}
		}
	}

	public void handleSnapshotEvent(String pair, OrderBookRecord record, boolean isAsk) {
		if (orderBooks.stream().anyMatch(orderBook -> orderBook.getCurrencyPair().equals(pair))) {
			// if orderbook for that currency pair already exists, add record to it
			for (KrakenOrderBook krakenOrderBook : orderBooks) {
				if (krakenOrderBook.getCurrencyPair().equals(pair)) {
					if (isAsk) {
						krakenOrderBook.addAsk(record);
					} else {
						krakenOrderBook.addBid(record);
					}
				}
			}
		} else {
			// if orderbook for that currency pair does not exist or orderbook is empty
			// create new book for this pair and add record to it
			KrakenOrderBook orderBook = new KrakenOrderBook(pair);
			if (isAsk) {
				orderBook.addAsk(record);
			} else {
				orderBook.addBid(record);
			}
			orderBooks.add(orderBook);
		}
	}

	public void printOrderBook() {
		Iterator<KrakenOrderBook> orderBookIterator = orderBooks.iterator();

		while (orderBookIterator.hasNext()) {
			KrakenOrderBook pairOrderBook = orderBookIterator.next();
			pairOrderBook.sort();

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

	public List<KrakenOrderBook> getOrderBooks() {
		return orderBooks;
	}

	public void addOrderBook(KrakenOrderBook orderBook) {
		this.orderBooks.add(orderBook);
	}
}
