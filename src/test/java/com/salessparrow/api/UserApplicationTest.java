package com.salessparrow.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.salessparrow.api.controllers.UserController;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserApplicationTest {

  @Autowired
  private UserController userController;

	@Test
	void contextLoads() {
        Assertions.assertThat(userController).isNotNull();
	}

  @Test
  void testGetDotenv() {
    
  }

  @Test
  void testMain() {
    
  }

}
