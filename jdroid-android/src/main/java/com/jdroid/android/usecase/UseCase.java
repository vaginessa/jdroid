package com.jdroid.android.usecase;

/**
 * 
 * @param <T> The Listener type
 */
public interface UseCase<T> extends Runnable {
	
	public void addListener(T listener);
	
	public void removeListener(T listener);
	
}
