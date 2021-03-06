package com.jdroid.android.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.MainThread;
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
import com.jdroid.android.loading.FragmentLoading;
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
		// TODO Should we create a new instance on rotation?
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
		return isHeroImageEnabled() ? R.layout.jdroid_base_hero_fragment : fragmentHelper.getBaseFragmentLayout();
	}

	@Override
	public Integer getContentFragmentLayout() {
		return fragmentHelper.getContentFragmentLayout();
	}

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

				Drawable appBarBackgroundDrawable = getResources().getDrawable(R.color.jdroid_colorPrimary).mutate();
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
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(ACTION_BAR_ALPHA, actionBarAlpha);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fragmentHelper.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		fragmentHelper.onStart();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		fragmentHelper.onResume();
	}
	
	@Override
	public void onPause() {
		fragmentHelper.onBeforePause();
		super.onPause();
		fragmentHelper.onPause();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		fragmentHelper.onStop();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		fragmentHelper.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		fragmentHelper.onBeforeDestroy();
		super.onDestroy();
		fragmentHelper.onDestroy();
	}
	
	@Override
	public <V extends View> V findView(int id) {
		return fragmentHelper.findView(id);
	}
	
	@Override
	public <V extends View> V findViewOnActivity(int id) {
		return fragmentHelper.findViewOnActivity(id);
	}
	
	@Override
	public View inflate(int resource) {
		return fragmentHelper.inflate(resource);
	}

	@MainThread
	@Override
	public void onStartUseCase() {
		fragmentHelper.onStartUseCase();
	}

	@MainThread
	@Override
	public void onUpdateUseCase() {
		fragmentHelper.onUpdateUseCase();
	}

	@MainThread
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		fragmentHelper.onFinishFailedUseCase(abstractException);
	}

	@Override
	public ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		return fragmentHelper.createErrorDisplayer(abstractException);
	}

	@MainThread
	@Override
	public void onFinishUseCase() {
		fragmentHelper.onFinishUseCase();
	}
	
	@Override
	public void executeOnUIThread(Runnable runnable) {
		fragmentHelper.executeOnUIThread(runnable);
	}
	
	@Override
	public <E> E getExtra(String key) {
		return fragmentHelper.getExtra(key);
	}
	
	@Override
	public <E> E getArgument(String key) {
		return fragmentHelper.getArgument(key);
	}
	
	@Override
	public <E> E getArgument(String key, E defaultValue) {
		return fragmentHelper.getArgument(key, defaultValue);
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
	
	@Override
	public void showLoading() {
		fragmentHelper.showLoading();
	}
	
	@Override
	public void dismissLoading() {
		fragmentHelper.dismissLoading();
	}
	
	@Override
	public FragmentLoading getDefaultLoading() {
		return fragmentHelper.getDefaultLoading();
	}
	
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
