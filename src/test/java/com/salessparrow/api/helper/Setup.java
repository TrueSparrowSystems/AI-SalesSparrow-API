package com.salessparrow.api.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

/**
 * This class is used to create the setup.
 */
@TestConfiguration
@Import({ DropTables.class })
public class Setup {

  Logger logger = LoggerFactory.getLogger(Setup.class);

  @Autowired
  private DropTables dropTables;
  
  /**
   * Create the setup.
   * - Flush the cache
   * - Drop dynamodb tables
   */
  public void perform() {
    logger.info("Creating setup");

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
   * Drop dynamodb tables.
   */
  private void dropTables() {
    dropTables.perform();
  }
}
