package com.jdroid.android.fragment;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.activity.ComponentIf;
import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.android.usecase.UseCase;
import com.jdroid.android.usecase.listener.DefaultUseCaseListener;
import com.jdroid.java.exception.AbstractException;

public interface FragmentIf extends ComponentIf, DefaultUseCaseListener, OnRefreshListener {
	
	public ActivityIf getActivityIf();

	public Boolean isSecondaryFragment();

	public Toolbar getAppBar();

	public void beforeInitAppBar(Toolbar appBar);

	public void afterInitAppBar(Toolbar appBar);

	public Integer getBaseFragmentLayout();

	public Integer getContentFragmentLayout();
	
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
	
	public Boolean goBackOnError(AbstractException abstractException);
	
	public Boolean shouldRetainInstance();
	
	public FragmentLoading getDefaultLoading();
	
	public void setLoading(FragmentLoading loading);
	
	public Boolean shouldTrackOnFragmentStart();
	
	public String getScreenViewName();
}
