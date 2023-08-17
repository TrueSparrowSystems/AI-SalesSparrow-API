package com.salessparrow.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // This enables asynchronous processing in Spring
public class SalesSparrowApi {

	public static void main(String[] args) {
		SpringApplication.run(SalesSparrowApi.class, args);
	}
}
