package com.salessparrow.api.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;

/**
 * This class is used to clean the setup after each test.
 */
@TestConfiguration
public class Cleanup {

	Logger logger = LoggerFactory.getLogger(Cleanup.class);

	@Autowired
	private DropTables dropTables;

	@Autowired
	private CacheManager cacheManager;

	/**
	 * Clean the setup after each test. - Flush the cache - Drop dynamodb tables
	 */
	public void perform() {
		logger.info("Cleaning setup");

		flushCache();
		dropTables();
	}

	/**
	 * Flush the cache.
	 */
	private void flushCache() {
		logger.info("Setup: Flushing cache");
		cacheManager.getCacheNames().stream().forEach(cacheName -> {
			cacheManager.getCache(cacheName).clear();
		});
	}

	/**
	 * Drop the tables.
	 */
	private void dropTables() {
		dropTables.perform();
	}

}
