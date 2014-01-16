package com.jdroid.java.concurrent;

/**
 * 
 * @author Maxi Rosson
 */
public class LowPriorityThreadFactory extends AbstractThreadFactory {
	
	public LowPriorityThreadFactory() {
		super("low-prio");
	}
	
	/**
	 * @see com.jdroid.java.concurrent.AbstractThreadFactory#getThreadsPriority()
	 */
	@Override
	protected int getThreadsPriority() {
		return Thread.MIN_PRIORITY;
	}
}