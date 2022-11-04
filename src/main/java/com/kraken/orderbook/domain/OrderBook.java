package com.kraken.orderbook.domain;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class OrderBook {

	private Long channelId;
	private Map<OrderBookPair, List<OrderBookRecord>> asks = Collections.emptyMap();
	private Map<OrderBookPair, List<OrderBookRecord>> bids = Collections.emptyMap();

	public void addAskRecord(OrderBookPair pair, OrderBookRecord record) {
		if (asks.containsKey(pair)) {
			asks.get(pair).add(record);
			Collections.sort(asks.get(pair));
		} else {			
			asks.put(pair, List.of(record));
		}
	}
	
	public void addBidsRecord(OrderBookPair pair, OrderBookRecord record) {
		if (bids.containsKey(pair)) {
			bids.get(pair).add(record);
			Collections.sort(asks.get(pair));
		} else {
			bids.put(pair, List.of(record));
		}
	}

}
