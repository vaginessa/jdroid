package com.jdroid.android.fragment;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.activity.ComponentIf;
import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.usecase.AbstractUseCase;
import com.jdroid.android.usecase.listener.UseCaseListener;
import com.jdroid.java.exception.AbstractException;

public interface FragmentIf extends ComponentIf, UseCaseListener, OnRefreshListener {
	
	public ActivityIf getActivityIf();

	public Boolean isSecondaryFragment();

	public Toolbar getAppBar();

	public void beforeInitAppBar(Toolbar appBar);

	public void afterInitAppBar(Toolbar appBar);

	@LayoutRes
	public Integer getBaseFragmentLayout();

	@LayoutRes
	public Integer getContentFragmentLayout();
	
	/**
	 * Finds a view that was identified by the id attribute from the XML that was processed in {@link Activity#onCreate}
	 * 
	 * @param id The id to search for.
	 * @param <V> The {@link View} class
	 * 
	 * @return The view if found or null otherwise.
	 */
	public <V extends View> V findViewOnActivity(@IdRes int id);
	
	/**
	 * @param key The key of the argument extra
	 * @param <E> The instance type
	 * @return the entry with the given key as an object.
	 */
	public <E> E getArgument(String key);
	
	public <E> E getArgument(String key, E defaultValue);
	
	public void onResumeUseCase(AbstractUseCase useCase, UseCaseListener listener);
	
	public void onResumeUseCase(AbstractUseCase useCase, UseCaseListener listener,
			UseCaseTrigger useCaseTrigger);
	
	public void onPauseUseCase(AbstractUseCase useCase, UseCaseListener listener);
	
	public void executeUseCase(AbstractUseCase useCase);
	
	public void executeUseCase(AbstractUseCase useCase, Long delaySeconds);
	
	public Boolean goBackOnError(AbstractException abstractException);
	
	public Boolean shouldRetainInstance();
	
	public FragmentLoading getDefaultLoading();
	
	public void setLoading(FragmentLoading loading);
	
	public Boolean shouldTrackOnFragmentStart();

	@NonNull
	public String getScreenViewName();
}
