package com.salessparrow.api.utility;

import com.salessparrow.api.config.CoreConstants;
import org.springframework.cache.interceptor.KeyGenerator;
import java.lang.reflect.Method;

/**
 * Cache Key Generator
 */
public class CacheKeyGenerator implements KeyGenerator {

	/**
	 * Generate the key based on the method name and the parameters. Append the
	 * environment name to the key.
	 * @param target
	 * @param method
	 * @param params
	 * @return String
	 */
	@Override
	public Object generate(Object target, Method method, Object... params) {
		String prefix = CoreConstants.environment();
		StringBuilder key = new StringBuilder();
		key.append(prefix);
		key.append(":");
		key.append(target.getClass().getSimpleName());
		key.append(":");
		key.append(method.getName());
		for (Object param : params) {
			key.append(":");
			key.append(param.toString());
		}
		return key.toString();
	};

}
