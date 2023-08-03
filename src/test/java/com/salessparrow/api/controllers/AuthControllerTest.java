package com.salessparrow.api.controllers;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.salessparrow.api.config.LocalStackConfiguration;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@Import(LocalStackConfiguration.class)
public class AuthControllerTest {

    static Logger logger = LoggerFactory.getLogger(AuthControllerTest.class);

    @BeforeAll
    public static void setUp() {
        logger.info("Setting up: create tables here");
    }

    @AfterAll
    public static void tearDown() {
        logger.info("Tearing down: delete tables here");
    }

    @Test
    public void testSalesforceRedirectUrlEndpoint() throws Exception{
       logger.info("Testing Salesforce redirect url endpoint");
    }
}

