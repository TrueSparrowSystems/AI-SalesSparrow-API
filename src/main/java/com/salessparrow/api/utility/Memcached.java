package com.salessparrow.api.utility;

import java.util.concurrent.Callable;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.transcoders.SerializingTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.elasticache.AmazonElastiCache;
import com.amazonaws.services.elasticache.AmazonElastiCacheClientBuilder;
import com.amazonaws.services.elasticache.model.CacheCluster;
import com.amazonaws.services.elasticache.model.DescribeCacheClustersRequest;
import com.amazonaws.services.elasticache.model.DescribeCacheClustersResult;
import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;

public class Memcached implements Cache {

  private static final Logger LOGGER = LoggerFactory.getLogger(Memcached.class);

  private String name;

  private MemcachedClient cache;

  private int expiration;

  public Memcached(String name, int expiration) {
    this.name = name;
    this.expiration = expiration;

    try {
      if (CoreConstants.isDevEnvironment()) {
        System.out.println("Using local memcached");
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
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Object getNativeCache() {
    return cache;
  }

  @Override
  public ValueWrapper get(final Object key) {
    String keyString = name + "_" + key.toString();
    Object value = null;
    try {
        value = cache.get(keyString);
    } catch (final Exception e) {
        LOGGER.warn(e.getMessage());
    }
    if (value == null) {
        LOGGER.info("cache miss for key: " + keyString);
        return null;
    }
    LOGGER.info("cache hit for key: " + keyString);

    return new SimpleValueWrapper(value);
  }

  @Override
  public void put(final Object key, final Object value) {
    String keyString = name + "_" + key.toString();
    if (value != null) {
      cache.set(keyString, expiration, value);
      LOGGER.info("cache put for key: " + keyString);
    }
  }


  @Override
  public void evict(final Object key) {
    String keyString = name + "_" + key.toString();
    this.cache.delete(keyString);
    LOGGER.info("cache delete for key: " + keyString);
  }

  @Override
  public void clear() {
    cache.flush();
    LOGGER.info("cache clear completed");
  }

  @Override
  public <T> T get(Object o, Class<T> aClass) {
    return null;
  }

  @Override
  public <T> T get(Object o, Callable<T> callable) {
    return null;
  }

  @Override
  public ValueWrapper putIfAbsent(Object o, Object o1) {
    return null;
  }
}