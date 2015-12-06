package com.jdroid.javaweb.guava.function;

import com.google.common.base.Function;
import com.jdroid.java.domain.Identifiable;

/**
 * A Function implementation that extracts the ID of an {@link Identifiable} object
 */
public class IdPropertyFunction implements Function<Identifiable, String> {
	
	/**
	 * @see com.jdroid.javaweb.guava.function.PropertyFunction#apply(java.lang.Object)
	 */
	@Override
	public String apply(Identifiable from) {
		return from.getId();
	}
}
