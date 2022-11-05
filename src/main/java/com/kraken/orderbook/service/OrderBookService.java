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

	public void addAskRecord(String pair, OrderBookRecord record) {
		if (orderBooks
				.stream()
				.anyMatch(b -> b.getCurrencyPair().equals(pair))) {
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

	public void addBidsRecord(String pair, OrderBookRecord record) {
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
