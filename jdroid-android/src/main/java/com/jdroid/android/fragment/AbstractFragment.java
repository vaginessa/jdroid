package com.jdroid.android.fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.google.android.gms.ads.AdSize;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.context.DefaultApplicationContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.loading.LoadingDialogBuilder;
import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.android.usecase.UseCase;
import com.jdroid.android.usecase.listener.DefaultUseCaseListener;

/**
 * Base {@link Fragment}
 * 
 * @author Maxi Rosson
 */
public abstract class AbstractFragment extends Fragment implements FragmentIf {
	
	private FragmentHelper fragmentHelper;
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAndroidApplicationContext()
	 */
	@Override
	public DefaultApplicationContext getAndroidApplicationContext() {
		return fragmentHelper.getAndroidApplicationContext();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#shouldRetainInstance()
	 */
	@Override
	public Boolean shouldRetainInstance() {
		return fragmentHelper.shouldRetainInstance();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentHelper = AbstractApplication.get().createFragmentHelper(this);
		fragmentHelper.onCreate(savedInstanceState);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		fragmentHelper.onViewCreated(view, savedInstanceState);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fragmentHelper.onActivityCreated(savedInstanceState);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		fragmentHelper.onStart();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		fragmentHelper.onResume();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		fragmentHelper.onBeforePause();
		super.onPause();
		fragmentHelper.onPause();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
		fragmentHelper.onStop();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		fragmentHelper.onDestroyView();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		fragmentHelper.onBeforeDestroy();
		super.onDestroy();
		fragmentHelper.onDestroy();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#findView(int)
	 */
	@Override
	public <V extends View> V findView(int id) {
		return fragmentHelper.findView(id);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#findViewOnActivity(int)
	 */
	@Override
	public <V extends View> V findViewOnActivity(int id) {
		return fragmentHelper.findViewOnActivity(id);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#inflate(int)
	 */
	@Override
	public View inflate(int resource) {
		return fragmentHelper.inflate(resource);
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		fragmentHelper.onStartUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onUpdateUseCase()
	 */
	@Override
	public void onUpdateUseCase() {
		fragmentHelper.onUpdateUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(java.lang.RuntimeException)
	 */
	@Override
	public void onFinishFailedUseCase(RuntimeException runtimeException) {
		fragmentHelper.onFinishFailedUseCase(runtimeException);
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		fragmentHelper.onFinishUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishCanceledUseCase()
	 */
	@Override
	public void onFinishCanceledUseCase() {
		fragmentHelper.onFinishCanceledUseCase();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#goBackOnError(java.lang.RuntimeException)
	 */
	@Override
	public Boolean goBackOnError(RuntimeException runtimeException) {
		return fragmentHelper.goBackOnError(runtimeException);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeOnUIThread(java.lang.Runnable)
	 */
	@Override
	public void executeOnUIThread(Runnable runnable) {
		fragmentHelper.executeOnUIThread(runnable);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showBlockingLoading()
	 */
	@Override
	public void showBlockingLoading() {
		fragmentHelper.showBlockingLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showBlockingLoading(com.jdroid.android.loading.LoadingDialogBuilder)
	 */
	@Override
	public void showBlockingLoading(LoadingDialogBuilder builder) {
		fragmentHelper.showBlockingLoading(builder);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissBlockingLoading()
	 */
	@Override
	public void dismissBlockingLoading() {
		fragmentHelper.dismissBlockingLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getInstance(java.lang.Class)
	 */
	@Override
	public <I> I getInstance(Class<I> clazz) {
		return fragmentHelper.<I>getInstance(clazz);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getExtra(java.lang.String)
	 */
	@Override
	public <E> E getExtra(String key) {
		return fragmentHelper.<E>getExtra(key);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getArgument(java.lang.String)
	 */
	@Override
	public <E> E getArgument(String key) {
		return fragmentHelper.<E>getArgument(key);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getArgument(java.lang.String, java.lang.Object)
	 */
	@Override
	public <E> E getArgument(String key, E defaultValue) {
		return fragmentHelper.<E>getArgument(key, defaultValue);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeUseCase(com.jdroid.android.usecase.UseCase)
	 */
	@Override
	public void executeUseCase(UseCase<?> useCase) {
		fragmentHelper.executeUseCase(useCase);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeUseCase(com.jdroid.android.usecase.UseCase, java.lang.Long)
	 */
	@Override
	public void executeUseCase(UseCase<?> useCase, Long delaySeconds) {
		fragmentHelper.executeUseCase(useCase, delaySeconds);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getUser()
	 */
	@Override
	public User getUser() {
		return fragmentHelper.getUser();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getActionBar()
	 */
	@Override
	public ActionBar getActionBar() {
		return fragmentHelper.getActionBar();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return fragmentHelper.getAdSize();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#onResumeUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase,
	 *      com.jdroid.android.usecase.listener.DefaultUseCaseListener)
	 */
	@Override
	public void onResumeUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener) {
		fragmentHelper.onResumeUseCase(useCase, listener);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#onResumeUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase,
	 *      com.jdroid.android.usecase.listener.DefaultUseCaseListener,
	 *      com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger)
	 */
	@Override
	public void onResumeUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener,
			UseCaseTrigger useCaseTrigger) {
		fragmentHelper.onResumeUseCase(useCase, listener, useCaseTrigger);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#onPauseUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase,
	 *      com.jdroid.android.usecase.listener.DefaultUseCaseListener)
	 */
	@Override
	public void onPauseUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener) {
		fragmentHelper.onPauseUseCase(useCase, listener);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#isBlockingLoadingEnabled()
	 */
	@Override
	public Boolean isBlockingLoadingEnabled() {
		return fragmentHelper.isBlockingLoadingEnabled();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading()
	 */
	@Override
	public void showLoading() {
		fragmentHelper.showLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissLoading()
	 */
	@Override
	public void dismissLoading() {
		fragmentHelper.dismissLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showNonBlockingLoading()
	 */
	@Override
	public void showNonBlockingLoading() {
		fragmentHelper.showNonBlockingLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissNonBlockingLoading()
	 */
	@Override
	public void dismissNonBlockingLoading() {
		fragmentHelper.dismissNonBlockingLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getActivityIf()
	 */
	@Override
	public ActivityIf getActivityIf() {
		return fragmentHelper.getActivityIf();
	}
}
