package com.jdroid.javaweb.cache;

import org.springframework.cache.interceptor.DefaultKeyGenerator;

import java.lang.reflect.Method;

public class CacheKeyGenerator extends DefaultKeyGenerator {
	
	@Override
	public Object generate(Object target, Method method, Object... params) {
		return method.getName() + super.generate(target, method, params).toString();
	}
}
