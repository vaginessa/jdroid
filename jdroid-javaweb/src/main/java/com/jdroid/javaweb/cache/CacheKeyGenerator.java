package com.jdroid.javaweb.cache;

import java.lang.reflect.Method;
import org.springframework.cache.interceptor.DefaultKeyGenerator;

/**
 * @author Maxi Rosson
 */
public class CacheKeyGenerator extends DefaultKeyGenerator {
	
	/**
	 * @see org.springframework.cache.interceptor.KeyGenerator#generate(java.lang.Object, java.lang.reflect.Method,
	 *      java.lang.Object[])
	 */
	@Override
	public Object generate(Object target, Method method, Object... params) {
		return method.getName() + super.generate(target, method, params).toString();
	}
}
