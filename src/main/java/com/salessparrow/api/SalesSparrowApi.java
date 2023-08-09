package com.salessparrow.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SalesSparrowApi {

	public static void main(String[] args) {
		SpringApplication.run(SalesSparrowApi.class, args);
	}
}
