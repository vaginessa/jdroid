package com.jdroid.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.ad.AdHelper;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.concurrent.SafeExecuteWrapperRunnable;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.exception.AbstractErrorDisplayer;
import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.usecase.AbstractUseCase;
import com.jdroid.android.usecase.listener.UseCaseListener;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class FragmentHelper implements FragmentIf {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(FragmentHelper.class);

	private Fragment fragment;
	private AdHelper adHelper;
	
	private FragmentLoading loading;

	private Toolbar appBar;
	
	public FragmentHelper(Fragment fragment) {
		this.fragment = fragment;
	}
	
	public FragmentIf getFragmentIf() {
		return (FragmentIf)fragment;
	}
	
	protected Fragment getFragment() {
		return fragment;
	}
	
	@Override
	public ActivityIf getActivityIf() {
		return (ActivityIf)fragment.getActivity();
	}
	
	public void onCreate(Bundle savedInstanceState) {
		LOGGER.debug("Executing onCreate on " + fragment);
		fragment.setRetainInstance(getFragmentIf().shouldRetainInstance());
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#shouldRetainInstance()
	 */
	@Override
	public Boolean shouldRetainInstance() {
		return true;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LOGGER.debug("Executing onCreateView on " + fragment);
		Integer contentFragmentLayout = getFragmentIf().getContentFragmentLayout();
		if (contentFragmentLayout != null) {
			View view = inflater.inflate(getFragmentIf().getBaseFragmentLayout(), container, false);
			ViewGroup viewGroup = (ViewGroup)view.findViewById(R.id.content);
			viewGroup.addView(inflater.inflate(contentFragmentLayout, null, false));
			return view;
		}
		return null;
	}

	@Override
	public Integer getBaseFragmentLayout() {
		return R.layout.base_fragment;
	}

	@Override
	public Integer getContentFragmentLayout() {
		return null;
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState) {
		LOGGER.debug("Executing onViewCreated on " + fragment);

		appBar = findView(R.id.appBar);
		if (appBar != null && getActivityIf() instanceof AbstractFragmentActivity) {
			getFragmentIf().beforeInitAppBar(appBar);

			if (getFragmentIf().isSecondaryFragment()) {
				if (getFragmentIf().getMenuResourceId() != null) {
					appBar.inflateMenu(getFragmentIf().getMenuResourceId());
					appBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem item) {
							return getFragmentIf().onOptionsItemSelected(item);
						}
					});
				}
			} else {
				((AbstractFragmentActivity)getActivityIf()).setSupportActionBar(appBar);
				appBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
				getActivityIf().initNavDrawer(appBar);
			}

			getFragmentIf().afterInitAppBar(appBar);
		}

		adHelper = getFragmentIf().createAdHelper();
		if (adHelper != null) {
			adHelper.setAdViewContainer((ViewGroup)(fragment.getView().findViewById(R.id.adViewContainer)));
			adHelper.loadBanner(fragment.getActivity());
		}
		
		if (loading == null) {
			loading = getFragmentIf().getDefaultLoading();
		}
		if (loading != null) {
			loading.onViewCreated(getFragmentIf());
		}
	}

	public Boolean isSecondaryFragment() {
		return false;
	}

	@Override
	public void beforeInitAppBar(Toolbar appBar) {
		// Do nothing
	}

	@Override
	public void afterInitAppBar(Toolbar appBar) {
		// Do nothing
	}

	@Override
	public Toolbar getAppBar() {
		return appBar;
	}

	@Override
	@Nullable
	public AdHelper createAdHelper() {
		return null;
	}

	@Nullable
	@Override
	public AdHelper getAdHelper() {
		return adHelper;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		LOGGER.debug("Executing onActivityCreated on " + fragment);
	}
	
	public void onStart() {
		LOGGER.debug("Executing onStart on " + fragment);
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf.shouldTrackOnFragmentStart()) {
			AbstractApplication.get().getAnalyticsSender().onFragmentStart(fragmentIf.getScreenViewName());
		}
	}
	
	public void onResume() {
		LOGGER.debug("Executing onResume on " + fragment);
		if (adHelper != null) {
			adHelper.onResume();
		}
	}
	
	public void onBeforePause() {
		if (adHelper != null) {
			adHelper.onPause();
		}
	}
	
	public void onPause() {
		LOGGER.debug("Executing onPause on " + fragment);
	}
	
	public void onStop() {
		LOGGER.debug("Executing onStop on " + fragment);
	}
	
	public void onDestroyView() {
		LOGGER.debug("Executing onDestroyView on " + fragment);
	}
	
	public void onBeforeDestroy() {
		if (adHelper != null) {
			adHelper.onDestroy();
		}
	}
	
	public void onDestroy() {
		LOGGER.debug("Executing onDestroy on " + fragment);
	}
	
	@Override
	public <E> E getArgument(String key) {
		return getArgument(key, null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <E> E getArgument(String key, E defaultValue) {
		Bundle arguments = fragment.getArguments();
		E value = (arguments != null) && arguments.containsKey(key) ? (E)arguments.get(key) : null;
		return value != null ? value : defaultValue;
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeOnUIThread(java.lang.Runnable)
	 */
	@Override
	public void executeOnUIThread(Runnable runnable) {
		Activity activity = fragment.getActivity();
		if ((activity != null) && activity.equals(AbstractApplication.get().getCurrentActivity())) {
			activity.runOnUiThread(new SafeExecuteWrapperRunnable(fragment, runnable));
		}
	}
	
	// Use case
	
	@Override
	public void onResumeUseCase(AbstractUseCase useCase, UseCaseListener listener) {
		onResumeUseCase(useCase, listener, UseCaseTrigger.MANUAL);
	}
	
	public void onResumeUseCase(final AbstractUseCase useCase, final UseCaseListener listener,
								final UseCaseTrigger useCaseTrigger) {
		if (useCase != null) {
			ExecutorUtils.execute(new Runnable() {

				@Override
				public void run() {
					useCase.addListener(listener);
					if (useCase.isInProgress()) {
						if (listener != null && !useCase.isNotified()) {
							listener.onStartUseCase();
						}
					} else if (useCase.isFinishSuccessful()) {
						if (listener != null && !useCase.isNotified()) {
							listener.onFinishUseCase();
							useCase.markAsNotified();
						}

						if (useCaseTrigger.equals(UseCaseTrigger.ALWAYS)) {
							useCase.run();
						}
					} else if (useCase.isFinishFailed()) {
						if (listener != null && !useCase.isNotified()) {
							try {
								listener.onFinishFailedUseCase(useCase.getAbstractException());
							} finally {
								useCase.markAsNotified();
							}
						}

						if (useCaseTrigger.equals(UseCaseTrigger.ALWAYS)) {
							useCase.run();
						}

					} else if (useCase.isNotInvoked()
							&& (useCaseTrigger.equals(UseCaseTrigger.ONCE) || useCaseTrigger.equals(UseCaseTrigger.ALWAYS))) {
						useCase.run();
					}
				}
			});
		}
	}
	
	public enum UseCaseTrigger {
		MANUAL,
		ONCE,
		ALWAYS;
	}
	
	@Override
	public void onPauseUseCase(final AbstractUseCase userCase, final UseCaseListener listener) {
		if (userCase != null) {
			userCase.removeListener(listener);
		}
	}
	
	@Override
	public void executeUseCase(AbstractUseCase useCase) {
		ExecutorUtils.execute(useCase);
	}
	
	@Override
	public void executeUseCase(AbstractUseCase useCase, Long delaySeconds) {
		ExecutorUtils.schedule(useCase, delaySeconds);
	}
	
	/**
	 * @see UseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		getFragmentIf().showLoading();
	}
	
	/**
	 * @see UseCaseListener#onUpdateUseCase()
	 */
	@Override
	public void onUpdateUseCase() {
		// Do nothing by default
	}
	
	/**
	 * @see UseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		getFragmentIf().dismissLoading();
	}
	
	/**
	 * @see UseCaseListener#onFinishFailedUseCase(com.jdroid.java.exception.AbstractException)
	 */
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		getFragmentIf().dismissLoading();
		getFragmentIf().createErrorDisplayer(abstractException).displayError(abstractException);
	}

	@Override
	public ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		return AbstractErrorDisplayer.getErrorDisplayer(abstractException);
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#findView(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <V extends View> V findView(int id) {
		return (V)fragment.getView().findViewById(id);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#findViewOnActivity(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <V extends View> V findViewOnActivity(int id) {
		return (V)fragment.getActivity().findViewById(id);
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#inflate(int)
	 */
	@Override
	public View inflate(int resource) {
		return getActivityIf().inflate(resource);
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#getInstance(java.lang.Class)
	 */
	@Override
	public <I> I getInstance(Class<I> clazz) {
		return getActivityIf().getInstance(clazz);
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#getExtra(java.lang.String)
	 */
	@Override
	public <E> E getExtra(String key) {
		return getActivityIf().getExtra(key);
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#getAppContext()
	 */
	@Override
	public AppContext getAppContext() {
		return getActivityIf().getAppContext();
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#getUser()
	 */
	@Override
	public User getUser() {
		return getActivityIf().getUser();
	}
	
	public Boolean isAuthenticated() {
		return SecurityContext.get().isAuthenticated();
	}
	
	// //////////////////////// Analytics //////////////////////// //
	
	@Override
	public Boolean shouldTrackOnFragmentStart() {
		return false;
	}
	
	@NonNull
	@Override
	public String getScreenViewName() {
		return fragment.getClass().getSimpleName();
	}
	
	// //////////////////////// Loading //////////////////////// //
	
	@Override
	public void showLoading() {
		final FragmentIf fragmentIf = getFragmentIf();
		fragmentIf.executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				if (loading != null) {
					loading.show(fragmentIf);
				} else {
					getActivityIf().showLoading();
				}
			}
		});
	}
	
	@Override
	public void dismissLoading() {
		final FragmentIf fragmentIf = getFragmentIf();
		fragmentIf.executeOnUIThread(new Runnable() {
				
			@Override
			public void run() {
				if (loading != null) {
					loading.dismiss(fragmentIf);
				} else {
					getActivityIf().dismissLoading();
				}
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getDefaultLoading()
	 */
	@Override
	public FragmentLoading getDefaultLoading() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#setLoading(com.jdroid.android.loading.FragmentLoading)
	 */
	@Override
	public void setLoading(FragmentLoading loading) {
		this.loading = loading;
	}
	
	/**
	 * @see android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener#onRefresh()
	 */
	@Override
	public void onRefresh() {
		// Do nothing
	}

	@Override
	public Integer getMenuResourceId() {
		return null;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}
}
