package com.jdroid.java.concurrent;

/**
 * 
 * @author Maxi Rosson
 */
public class NormalPriorityThreadFactory extends AbstractThreadFactory {
	
	public NormalPriorityThreadFactory(String namePrefix) {
		super(namePrefix);
	}
	
	public NormalPriorityThreadFactory() {
		super("normal-prio");
	}
	
	/**
	 * @see com.jdroid.java.concurrent.AbstractThreadFactory#getThreadsPriority()
	 */
	@Override
	protected int getThreadsPriority() {
		return Thread.NORM_PRIORITY;
	}
}