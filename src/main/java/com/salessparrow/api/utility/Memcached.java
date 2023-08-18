package com.salessparrow.api.utility;

import java.util.concurrent.Callable;
import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * Memcached implementation of the Cache interface.
 */
public class Memcached implements Cache {

  private static final Logger logger = LoggerFactory.getLogger(Memcached.class);

  private String name;

  private MemcachedClient cache;

  private int expiration;

  /**
   * Constructor
   * 
   * @param name
   * @param expiration
   */
  public Memcached(String name, int expiration, MemcachedClient memcachedClient) {
    this.name = name;
    this.expiration = expiration;
    this.cache = memcachedClient;
  }

  /**
   * Returns the name of the cache.
   * 
   * @param name
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Returns the native cache.
   * 
   * @param name
   */
  @Override
  public Object getNativeCache() {
    return cache;
  }

  /**
   * Returns the value associated with the key.
   * 
   * @param key
   */
  @Override
  public ValueWrapper get(final Object key) {
    String keyString = name + "_" + key.toString();
    Object value = null;
    try {
        value = cache.get(keyString);
    } catch (final Exception e) {
        logger.warn(e.getMessage());
    }
    if (value == null) {
        logger.debug("cache miss for key: " + keyString);
        return null;
    }
    logger.debug("cache hit for key: " + keyString);

    return new SimpleValueWrapper(value);
  }

  /**
   * Associates the value with the key.
   * 
   * @param key
   * @param value
   */
  @Override
  public void put(final Object key, final Object value) {
    String keyString = name + "_" + key.toString();
    if (value != null) {
      cache.set(keyString, expiration, value);
      logger.debug("cache put for key: " + keyString);
    }
  }

  /**
   * Removes the key and its associated value.
   * 
   * @param key
   */
  @Override
  public void evict(final Object key) {
    String keyString = name + "_" + key.toString();
    this.cache.delete(keyString);
    logger.debug("cache delete for key: " + keyString);
  }

  /**
   * Clears the cache.
   * 
   * @param key
   */
  @Override
  public void clear() {
    cache.flush();
    logger.debug("cache clear completed");
  }

  /**
   * Returns the value associated with the key.
   * 
   * @param key
   * @param aClass
   */
  @Override
  public <T> T get(Object o, Class<T> aClass) {
    return null;
  }

  /**
   * Returns the value associated with the key.
   * 
   * @param key
   * @param callable
   */
  @Override
  public <T> T get(Object o, Callable<T> callable) {
    return null;
  }

  /**
   * Associates the value with the key if absent.
   * 
   * @param key
   * @param value
   */
  @Override
  public ValueWrapper putIfAbsent(Object o, Object o1) {
    return null;
  }
}