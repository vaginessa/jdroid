package com.jdroid.android.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.activity.ComponentIf;
import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.android.usecase.UseCase;
import com.jdroid.android.usecase.listener.DefaultUseCaseListener;

public interface FragmentIf extends ComponentIf, DefaultUseCaseListener, OnRefreshListener {
	
	public ActivityIf getActivityIf();
	
	/**
	 * Finds a view that was identified by the id attribute from the XML that was processed in {@link Activity#onCreate}
	 * 
	 * @param id The id to search for.
	 * @param <V> The {@link View} class
	 * 
	 * @return The view if found or null otherwise.
	 */
	public <V extends View> V findViewOnActivity(int id);
	
	/**
	 * @param key The key of the argument extra
	 * @param <E> The instance type
	 * @return the entry with the given key as an object.
	 */
	public <E> E getArgument(String key);
	
	public <E> E getArgument(String key, E defaultValue);
	
	public void onResumeUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener);
	
	public void onResumeUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener,
			UseCaseTrigger useCaseTrigger);
	
	public void onPauseUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener);
	
	public void executeUseCase(UseCase<?> useCase);
	
	public void executeUseCase(UseCase<?> useCase, Long delaySeconds);
	
	public Boolean goBackOnError(RuntimeException runtimeException);
	
	public Boolean shouldRetainInstance();
	
	public ActionBar getActionBar();
	
	public FragmentLoading getDefaultLoading();
	
	public void setLoading(FragmentLoading loading);
}
