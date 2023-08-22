package com.salessparrow.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

import com.salessparrow.api.config.CoreConstants;

@SpringBootApplication
@EnableCaching // This enables caching
@EnableAsync // This enables asynchronous processing in Spring
public class SalesSparrowApi {

	public static void main(String[] args) {

		System.out.println("Env variables ---- environment: " + CoreConstants.environment());
		System.out.println("Env variables ---- aws region: " + CoreConstants.awsRegion());
		SpringApplication.run(SalesSparrowApi.class, args);
	}
}
