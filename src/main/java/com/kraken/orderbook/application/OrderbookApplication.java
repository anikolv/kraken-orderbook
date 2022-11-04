package com.kraken.orderbook.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@ComponentScan({ "com.kraken" })
@EnableWebSocket
public class OrderbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderbookApplication.class, args);
	}

}
