package com.salessparrow.api.onetimer;

import org.springframework.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.lib.globalConstants.CacheConstants;

import jakarta.annotation.PostConstruct;

@Component
public class CacheEvict {

  private final CacheManager cacheManager;

  @Autowired
  public CacheEvict(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  @PostConstruct
  public void evictCachesOnStartup() {
    // Clear specific caches
    System.out.println("Cache clear started");
    cacheManager.getCache(CacheConstants.SS_SALESFORCE_OAUTH_TOKEN_CACHE).clear();
    cacheManager.getCache(CacheConstants.SS_SALESFORCE_USER_CACHE).clear();
    System.out.println("Cache clear ended");
  }
}