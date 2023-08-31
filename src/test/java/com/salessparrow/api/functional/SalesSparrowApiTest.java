package com.salessparrow.api.functional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SalesSparrowApiTest {

	@Autowired
	private ApplicationContext ctx;

	@Test
	public void contextLoads() {
		assertThat(ctx).isNotNull();
	}

}
