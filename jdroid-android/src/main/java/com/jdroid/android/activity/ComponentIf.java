package com.jdroid.android.activity;

import android.support.v4.app.Fragment;
import android.view.View;
import com.google.android.gms.ads.AdSize;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.loading.LoadingDialog;
import com.jdroid.android.loading.LoadingDialogBuilder;

public interface ComponentIf {
	
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
	
	/**
	 * Show a blocking {@link LoadingDialog} in the current Thread.
	 */
	public void showBlockingLoading();
	
	public void showBlockingLoading(LoadingDialogBuilder builder);
	
	/**
	 * Dismiss the {@link LoadingDialog} in the current Thread
	 */
	public void dismissBlockingLoading();
	
	public AppContext getAppContext();
	
	public AdSize getAdSize();
	
	public User getUser();
}
