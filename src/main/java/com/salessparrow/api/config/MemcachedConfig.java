
package com.salessparrow.api.config;

import java.util.ArrayList;
import java.util.Collection;

import com.salessparrow.api.lib.globalConstants.CacheConstants;
import com.salessparrow.api.utility.Memcached;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemcachedConfig implements CachingConfigurer {

  @Override
  @Bean
  public CacheManager cacheManager() {
    SimpleCacheManager cacheManager = new SimpleCacheManager();
    
    cacheManager.setCaches(internalCaches());
    return cacheManager;
  }

  private Collection<Memcached> internalCaches() {
    final Collection<Memcached> caches = new ArrayList<>();
    caches.add(new Memcached(
        CacheConstants.SALESFORCE_USER_CACHE, 
        CacheConstants.SALESFORCE_USER_CACHE_EXP
      ));
    caches.add(
      new Memcached(
        CacheConstants.SALESFORCE_ORG_CACHE, 
        CacheConstants.SALESFORCE_ORG_CACHE_EXP
      ));
    caches.add(
      new Memcached(
        CacheConstants.SALESFORCE_OAUTH_TOKEN_CACHE, 
        CacheConstants.SALESFORCE_OAUTH_TOKEN_CACHE_EXP
      ));
    return caches;
  }

  @Override
  public KeyGenerator keyGenerator() {
    return new SimpleKeyGenerator();
  }

  @Override
  public CacheErrorHandler errorHandler() {
    return new SimpleCacheErrorHandler();
  }

  @Override
  public CacheResolver cacheResolver() {
    return null;
  }
}