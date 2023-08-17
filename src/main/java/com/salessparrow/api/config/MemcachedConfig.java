
package com.salessparrow.api.config;

import java.util.ArrayList;
import java.util.Collection;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.elasticache.AmazonElastiCache;
import com.amazonaws.services.elasticache.AmazonElastiCacheClientBuilder;
import com.amazonaws.services.elasticache.model.CacheCluster;
import com.amazonaws.services.elasticache.model.DescribeCacheClustersRequest;
import com.amazonaws.services.elasticache.model.DescribeCacheClustersResult;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.CacheConstants;
import com.salessparrow.api.utility.Memcached;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.transcoders.SerializingTranscoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Configuration for Memcached.
 */
@Configuration
public class MemcachedConfig implements CachingConfigurer {

  private MemcachedClient cache;

  private static final Logger logger = LoggerFactory.getLogger(MemcachedConfig.class);

  /**
   * Cache Manager Bean to initialize the cache client.
   * 
   * @return CacheManager
   */
  @Override
  @Bean
  public CacheManager cacheManager() {
    SimpleCacheManager cacheManager = new SimpleCacheManager();

    MemcachedClient cache = memcachedClient();
    
    cacheManager.setCaches(internalCaches(cache));
    return cacheManager;
  }

  /**
   * Internal Caches
   * All caches needs to be added here along with their expiry time.
   * 
   * @return Collection<Memcached>
   */
  private Collection<Memcached> internalCaches(MemcachedClient cache) {
    final Collection<Memcached> caches = new ArrayList<>();

    caches.add(new Memcached(
        CacheConstants.SALESFORCE_USER_CACHE, 
        CacheConstants.SALESFORCE_USER_CACHE_EXP,
        cache
      ));
    caches.add(
      new Memcached(
        CacheConstants.SALESFORCE_OAUTH_TOKEN_CACHE, 
        CacheConstants.SALESFORCE_OAUTH_TOKEN_CACHE_EXP,
        cache
      ));
    return caches;
  }

  public MemcachedClient memcachedClient() {
    try {
      if (CoreConstants.isDevEnvironment()) {
        logger.info("Using local memcached");
        // Local environment, use the provided memcachedAddresses
        cache = new MemcachedClient(
          new ConnectionFactoryBuilder()
            .setTranscoder(new SerializingTranscoder())
            .setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
            .build(),
          AddrUtil.getAddresses(CoreConstants.memcachedAddress()));
      } else {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
          CoreConstants.awsAccessKeyId(), 
          CoreConstants.awsSecretAccessKey()
        );

        AmazonElastiCache amazonElastiCache = AmazonElastiCacheClientBuilder.standard()
          .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
          .withRegion(CoreConstants.awsRegion())
          .build();

        DescribeCacheClustersResult clustersResult = amazonElastiCache.describeCacheClusters(
                new DescribeCacheClustersRequest().withCacheClusterId(CoreConstants.cacheClusterId()));
        CacheCluster cluster = clustersResult.getCacheClusters().get(0);
        String endpoint = cluster.getConfigurationEndpoint().getAddress();
        int port = cluster.getConfigurationEndpoint().getPort();

        cache = new MemcachedClient(new ConnectionFactoryBuilder()
                .setTranscoder(new SerializingTranscoder())
                .setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
                .build(),
                AddrUtil.getAddresses(endpoint + ":" + port));
      }
    } catch (Exception e) {
      throw new CustomException(
        new ErrorObject(
          "u_m_m_1",
          "something_went_wrong",
          e.getMessage()));
    } 

    return cache;
  } 

  /**
   * Key Generator
   * 
   * @return KeyGenerator
   */
  @Override
  public KeyGenerator keyGenerator() {
    return new SimpleKeyGenerator();
  }

  /**
   * Error Handler
   * 
   * @return CacheErrorHandler
   */
  @Override
  public CacheErrorHandler errorHandler() {
    return new SimpleCacheErrorHandler();
  }

  /**
   * Cache Resolver
   * 
   * @return CacheResolver
   */
  @Override
  public CacheResolver cacheResolver() {
    return null;
  }
}