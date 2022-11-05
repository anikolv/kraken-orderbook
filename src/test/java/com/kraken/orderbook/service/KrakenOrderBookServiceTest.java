package com.kraken.orderbook.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.kraken.orderbook.domain.KrakenOrderBook;
import com.kraken.orderbook.domain.OrderBookRecord;

@RunWith(SpringRunner.class)
public class KrakenOrderBookServiceTest {
	
	private KrakenOrderBookService orderBookService = new KrakenOrderBookService();
	private static Double PRICE = 2.345;
	private static Double VOLUME = 4.322;
	private static String PAIR = "ETC/USD";

	@Test
	public void testHandleAskSnapshotEvent() {
		orderBookService.handleSnapshotEvent(PAIR, new OrderBookRecord(PRICE, VOLUME), true);
		
		List<KrakenOrderBook> orderBooks = orderBookService.getOrderBooks();
		assertTrue(orderBooks.size() == 1);
		assertEquals(PAIR, orderBooks.stream().findFirst().get().getCurrencyPair());
		assertTrue(orderBooks.stream().findFirst().get().getAsks().size() == 1);
		assertEquals(PRICE, orderBooks.stream().findFirst().get().getAsks().stream().findFirst().get().getPrice());
		assertEquals(VOLUME, orderBooks.stream().findFirst().get().getAsks().stream().findFirst().get().getVolume());
	}
	
	@Test
	public void testHandleBidSnapshotEvent() {
		orderBookService.handleSnapshotEvent(PAIR, new OrderBookRecord(PRICE, VOLUME), false);
		
		List<KrakenOrderBook> orderBooks = orderBookService.getOrderBooks();
		assertTrue(orderBooks.size() == 1);
		assertEquals(PAIR, orderBooks.stream().findFirst().get().getCurrencyPair());
		assertTrue(orderBooks.stream().findFirst().get().getBids().size() == 1);
		assertEquals(PRICE, orderBooks.stream().findFirst().get().getBids().stream().findFirst().get().getPrice());
		assertEquals(VOLUME, orderBooks.stream().findFirst().get().getBids().stream().findFirst().get().getVolume());
	}
	
	@Test
	public void testHandleUpdateEvent() {		
		//test insert handler
		orderBookService.addOrderBook(new KrakenOrderBook(PAIR));
		orderBookService.handleUpdateEvent(PAIR, new OrderBookRecord(PRICE, VOLUME), true);
		
		List<KrakenOrderBook> orderBooks = orderBookService.getOrderBooks();
		
		assertTrue(orderBooks.size() == 1);
		assertEquals(PAIR, orderBooks.stream().findFirst().get().getCurrencyPair());
		assertTrue(orderBooks.stream().findFirst().get().getAsks().size() == 1);
		assertEquals(PRICE, orderBooks.stream().findFirst().get().getAsks().stream().findFirst().get().getPrice());
		assertEquals(VOLUME, orderBooks.stream().findFirst().get().getAsks().stream().findFirst().get().getVolume());
		
		//test update handler
		Double newVolume = 6.322;
		orderBookService.handleUpdateEvent(PAIR, new OrderBookRecord(PRICE, newVolume), true);
		
		assertTrue(orderBooks.size() == 1);
		assertEquals(PAIR, orderBooks.stream().findFirst().get().getCurrencyPair());
		assertTrue(orderBooks.stream().findFirst().get().getAsks().size() == 1);
		assertEquals(PRICE, orderBooks.stream().findFirst().get().getAsks().stream().findFirst().get().getPrice());
		assertEquals(newVolume, orderBooks.stream().findFirst().get().getAsks().stream().findFirst().get().getVolume());
		
		//test delete handler
		Double zeroVolume = 0.0;
		orderBookService.handleUpdateEvent(PAIR, new OrderBookRecord(PRICE, zeroVolume), true);
		
		assertTrue(orderBooks.size() == 1);
		assertEquals(PAIR, orderBooks.stream().findFirst().get().getCurrencyPair());
		assertTrue(orderBooks.stream().findFirst().get().getAsks().isEmpty());
	}

}
