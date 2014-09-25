package com.jdroid.android.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import com.google.android.gms.ads.AdSize;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.domain.User;

public interface ComponentIf {
	
	/**
	 * Runs the specified action on the UI thread. If the current thread is the UI thread, then the action is executed
	 * immediately. If the current thread is not the UI thread, the action is posted to the event queue of the UI
	 * thread.
	 * 
	 * If the current {@link Activity} is not equals to this, then no action is executed
	 * 
	 * @param runnable the action to run on the UI thread
	 */
	public void executeOnUIThread(Runnable runnable);
	
	/**
	 * Finds a view that was identified by the id attribute from the XML that was processed in
	 * {@link Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)}
	 * 
	 * @param id The id to search for.
	 * @param <V> The {@link View} class
	 * 
	 * @return The view if found or null otherwise.
	 */
	public <V extends View> V findView(int id);
	
	/**
	 * Inflate a new view hierarchy from the specified xml resource.
	 * 
	 * @param resource ID for an XML layout resource to load
	 * @return The root View of the inflated XML file.
	 */
	public View inflate(int resource);
	
	/**
	 * @param clazz The {@link Class}
	 * @param <I> The instance type
	 * @return An instance of the clazz
	 */
	public <I> I getInstance(Class<I> clazz);
	
	/**
	 * @param key The key of the intent extra
	 * @param <E> The instance type
	 * @return the entry with the given key as an object.
	 */
	public <E> E getExtra(String key);
	
	public AppContext getAppContext();
	
	public AdSize getAdSize();
	
	public User getUser();
	
	public Boolean shouldTrackOnFragmentStart();
	
	public void showLoading();
	
	public void dismissLoading();
}
