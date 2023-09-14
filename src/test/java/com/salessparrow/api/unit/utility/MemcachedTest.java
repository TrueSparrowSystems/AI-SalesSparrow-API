package com.salessparrow.api.unit.utility;

import net.spy.memcached.MemcachedClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;

import com.salessparrow.api.utility.Memcached;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemcachedTest {

	@Mock
	private MemcachedClient memcachedClient;

	private Cache memcachedCache;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		memcachedCache = new Memcached("testCache", 3600, memcachedClient);
	}

	@Test
	void testGetName() {
		assertEquals("testCache", memcachedCache.getName());
	}

	@Test
	void testGetNativeCache() {
		assertEquals(memcachedClient, memcachedCache.getNativeCache());
	}

	@Test
	void testGetCacheHit() {
		String key = "testKey";
		String value = "testValue";
		when(memcachedClient.get("testCache_" + key)).thenReturn(value);

		Cache.ValueWrapper result = memcachedCache.get(key);

		assertNotNull(result);
		assertEquals(value, result.get());
		verify(memcachedClient, times(1)).get("testCache_" + key);
	}

	@Test
	void testGetCacheMiss() {
		String key = "nonExistentKey";
		when(memcachedClient.get("testCache_" + key)).thenReturn(null);

		Cache.ValueWrapper result = memcachedCache.get(key);

		assertNull(result);
		verify(memcachedClient, times(1)).get("testCache_" + key);
	}

	@Test
	void testPut() {
		String key = "testKey";
		String value = "testValue";

		memcachedCache.put(key, value);

		verify(memcachedClient, times(1)).set("testCache_" + key, 3600, value);
	}

	@Test
	void testEvict() {
		String key = "testKey";

		memcachedCache.evict(key);

		verify(memcachedClient, times(1)).delete("testCache_" + key);
	}

	@Test
	void testClear() {
		memcachedCache.clear();

		verify(memcachedClient, times(1)).flush();
	}

}
