package com.salessparrow.api.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

/**
 * This class is used to clean the setup after each test.
 */
@TestConfiguration
public class Cleanup {

  Logger logger = LoggerFactory.getLogger(Cleanup.class);

  @Autowired
  private DropTables dropTables;

  /**
   * Clean the setup after each test.
   * - Flush the cache
   * - Drop dynamodb tables
   */
  public void perform() {
    logger.info("Cleaning setup");

    flushCache();
    dropTables();
  }

  /**
   * Flush the cache.
   */
  private void flushCache(){
    logger.info("Setup: Flushing cache");
  }

  /**
   * Drop the tables.
   */
  private void dropTables() {
    dropTables.perform();
  }
}
