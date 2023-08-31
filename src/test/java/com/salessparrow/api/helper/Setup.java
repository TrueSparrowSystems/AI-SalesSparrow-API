package com.salessparrow.api.helper;

import org.springframework.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

import com.github.dynamobee.Dynamobee;
import com.github.dynamobee.exception.DynamobeeException;

/**
 * This class is used to create the setup.
 */
@TestConfiguration
@Import({ DropTables.class })
public class Setup {

	Logger logger = LoggerFactory.getLogger(Setup.class);

	@Autowired
	private DropTables dropTables;

	@Autowired
	private Dynamobee dynamobee;

	@Autowired
	private CacheManager cacheManager;

	/**
	 * Create the setup. - Flush the cache - Drop dynamodb tables - Run migrations
	 * @throws DynamobeeException
	 */
	public void perform() throws DynamobeeException {
		logger.info("Creating setup");

		flushCache();
		dropTables();
		runMigrations();
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
	 * Drop dynamodb tables.
	 */
	private void dropTables() {
		dropTables.perform();
	}

	/**
	 * Run migrations.
	 * @throws DynamobeeException
	 */
	public void runMigrations() throws DynamobeeException {
		dynamobee.execute();
	}

}
