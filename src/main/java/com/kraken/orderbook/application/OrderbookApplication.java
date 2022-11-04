package com.kraken.orderbook.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.kraken" })
public class OrderbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderbookApplication.class, args);
	}

}
