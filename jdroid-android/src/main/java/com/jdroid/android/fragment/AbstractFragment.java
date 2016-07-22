package com.jdroid.android.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.android.R;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.usecase.AbstractUseCase;
import com.jdroid.android.usecase.listener.UseCaseListener;
import com.jdroid.android.view.NotifyingScrollView;
import com.jdroid.android.view.ParallaxScrollView;
import com.jdroid.java.exception.AbstractException;

/**
 * Base {@link Fragment}
 * 
 */
public abstract class AbstractFragment extends Fragment implements FragmentIf {
	
	private static final String ACTION_BAR_ALPHA = "actionBarAlpha";
	
	private FragmentHelper fragmentHelper;
	private int actionBarAlpha = 0;

	@Override
	public Boolean shouldRetainInstance() {
		return fragmentHelper.shouldRetainInstance();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentHelper = AbstractApplication.get().createFragmentHelper(this);
		fragmentHelper.onCreate(savedInstanceState);
	}

	@Override
	public void onNewIntent(Intent intent) {
		fragmentHelper.onNewIntent(intent);
	}
	
	protected Boolean isHeroImageEnabled() {
		return false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = fragmentHelper.onCreateView(inflater, container, savedInstanceState);
		return view != null ? view : super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public Integer getBaseFragmentLayout() {
		return isHeroImageEnabled() ? R.layout.base_hero_fragment : fragmentHelper.getBaseFragmentLayout();
	}

	@Override
	public Integer getContentFragmentLayout() {
		return fragmentHelper.getContentFragmentLayout();
	}

	/**
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		fragmentHelper.onViewCreated(view, savedInstanceState);
		
		if (isHeroImageEnabled()) {
			final Toolbar appBar = getAppBar();
			if (appBar != null) {

				if (savedInstanceState != null) {
					actionBarAlpha = savedInstanceState.getInt(ACTION_BAR_ALPHA);
				}

				Drawable appBarBackgroundDrawable = getResources().getDrawable(R.color.colorPrimary).mutate();
				appBar.setBackgroundDrawable(appBarBackgroundDrawable);
				appBarBackgroundDrawable.setAlpha(actionBarAlpha);

				Integer parallaxScrollViewId = getParallaxScrollViewId();
				if (parallaxScrollViewId != null) {
					ParallaxScrollView parallaxScrollView = findView(parallaxScrollViewId);
					parallaxScrollView.setOnScrollChangedListener(new NotifyingScrollView.OnScrollChangedListener() {

						@Override
						public void onScrollChanged(NestedScrollView who, int l, int t, int oldl, int oldt) {
							final int headerHeight = findView(getHeroImageId()).getHeight() - appBar.getHeight();
							final float ratio = (float)Math.min(Math.max(t, 0), headerHeight) / headerHeight;
							actionBarAlpha = (int)(ratio * 255);
							appBar.getBackground().setAlpha(actionBarAlpha);
						}
					});
					parallaxScrollView.setParallaxViewContainer(findView(getHeroImageContainerId()));
				}
			}
		}
	}

	protected Integer getParallaxScrollViewId() {
		return null;
	}
	
	protected Integer getHeroImageContainerId() {
		return null;
	}
	
	protected Integer getHeroImageId() {
		return null;
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(ACTION_BAR_ALPHA, actionBarAlpha);
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
	
	@Override
	public void onStartUseCase() {
		fragmentHelper.onStartUseCase();
	}
	
	@Override
	public void onUpdateUseCase() {
		fragmentHelper.onUpdateUseCase();
	}
	
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		fragmentHelper.onFinishFailedUseCase(abstractException);
	}

	@Override
	public ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		return fragmentHelper.createErrorDisplayer(abstractException);
	}
	
	@Override
	public void onFinishUseCase() {
		fragmentHelper.onFinishUseCase();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeOnUIThread(java.lang.Runnable)
	 */
	@Override
	public void executeOnUIThread(Runnable runnable) {
		fragmentHelper.executeOnUIThread(runnable);
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
	
	@Override
	public void executeUseCase(AbstractUseCase useCase) {
		fragmentHelper.executeUseCase(useCase);
	}
	
	@Override
	public void executeUseCase(AbstractUseCase useCase, Long delaySeconds) {
		fragmentHelper.executeUseCase(useCase, delaySeconds);
	}
	
	@Override
	public void beforeInitAppBar(Toolbar appBar) {
		fragmentHelper.beforeInitAppBar(appBar);
	}

	@Override
	public void afterInitAppBar(Toolbar appBar) {
		fragmentHelper.afterInitAppBar(appBar);
	}

	@Override
	public Toolbar getAppBar() {
		return fragmentHelper.getAppBar();
	}

	@Override
	public void registerUseCase(AbstractUseCase useCase, UseCaseListener listener) {
		fragmentHelper.registerUseCase(useCase, listener);
	}
	
	@Override
	public void registerUseCase(AbstractUseCase useCase, UseCaseListener listener,
								UseCaseTrigger useCaseTrigger) {
		fragmentHelper.registerUseCase(useCase, listener, useCaseTrigger);
	}
	
	@Override
	public void unregisterUseCase(AbstractUseCase useCase, UseCaseListener listener) {
		fragmentHelper.unregisterUseCase(useCase, listener);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getActivityIf()
	 */
	@Override
	public ActivityIf getActivityIf() {
		return fragmentHelper.getActivityIf();
	}
	
	// //////////////////////// Analytics //////////////////////// //
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#shouldTrackOnFragmentStart()
	 */
	@Override
	public Boolean shouldTrackOnFragmentStart() {
		return fragmentHelper.shouldTrackOnFragmentStart();
	}
	
	@NonNull
	@Override
	public String getScreenViewName() {
		return fragmentHelper.getScreenViewName();
	}
	
	// //////////////////////// Loading //////////////////////// //
	
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
	 * @see com.jdroid.android.fragment.FragmentIf#getDefaultLoading()
	 */
	@Override
	public FragmentLoading getDefaultLoading() {
		return fragmentHelper.getDefaultLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#setLoading(com.jdroid.android.loading.FragmentLoading)
	 */
	@Override
	public void setLoading(FragmentLoading loading) {
		fragmentHelper.setLoading(loading);
	}
	
	@Override
	public Integer getMenuResourceId() {
		return fragmentHelper.getMenuResourceId();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return fragmentHelper.onOptionsItemSelected(item);
	}

	@Override
	public Boolean isSecondaryFragment() {
		return fragmentHelper.isSecondaryFragment();
	}

	@Override
	public FragmentDelegate createFragmentDelegate(AppModule appModule) {
		return fragmentHelper.createFragmentDelegate(appModule);
	}

	@Override
	public FragmentDelegate getFragmentDelegate(AppModule appModule) {
		return fragmentHelper.getFragmentDelegate(appModule);
	}

	@Override
	public Boolean onBackPressedHandled() {
		return fragmentHelper.onBackPressedHandled();
	}
}
