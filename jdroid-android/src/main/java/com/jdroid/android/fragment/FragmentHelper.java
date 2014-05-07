package com.jdroid.android.fragment;

import org.slf4j.Logger;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.google.android.gms.ads.AdSize;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.ad.AdHelper;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.loading.LoadingDialogBuilder;
import com.jdroid.android.loading.LoadingLayout;
import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.android.usecase.UseCase;
import com.jdroid.android.usecase.listener.DefaultUseCaseListener;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class FragmentHelper implements FragmentIf {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(FragmentHelper.class);
	
	private Fragment fragment;
	private AdHelper adHelper;
	private LoadingLayout loadingLayout;
	
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
		LOGGER.trace("Executing onCreate on " + fragment);
		fragment.setRetainInstance(getFragmentIf().shouldRetainInstance());
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#shouldRetainInstance()
	 */
	@Override
	public Boolean shouldRetainInstance() {
		return true;
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState) {
		LOGGER.trace("Executing onViewCreated on " + fragment);
		
		adHelper = createAdLoader();
		if (adHelper != null) {
			adHelper.loadAd(fragment.getActivity(), (ViewGroup)(fragment.getView().findViewById(R.id.adViewContainer)),
				getFragmentIf().getAdSize(), getRemoveAdsClickListener());
		}
		
		loadingLayout = findView(R.id.container);
		if (loadingLayout != null) {
			loadingLayout.setLoading(getFragmentIf().isNonBlockingLoadingDisplayedByDefault());
		}
	}
	
	protected AdHelper createAdLoader() {
		return new AdHelper();
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		LOGGER.trace("Executing onActivityCreated on " + fragment);
	}
	
	public void onStart() {
		LOGGER.trace("Executing onStart on " + fragment);
	}
	
	public void onResume() {
		LOGGER.trace("Executing onResume on " + fragment);
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
		LOGGER.trace("Executing onPause on " + fragment);
	}
	
	public void onStop() {
		LOGGER.trace("Executing onStop on " + fragment);
	}
	
	public void onDestroyView() {
		LOGGER.trace("Executing onDestroyView on " + fragment);
	}
	
	public void onBeforeDestroy() {
		if (adHelper != null) {
			adHelper.onDestroy();
		}
	}
	
	public void onDestroy() {
		LOGGER.trace("Executing onDestroy on " + fragment);
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
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#onResumeUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase,
	 *      com.jdroid.android.usecase.listener.DefaultUseCaseListener)
	 */
	@Override
	public void onResumeUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener) {
		onResumeUseCase(useCase, listener, UseCaseTrigger.MANUAL);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#onResumeUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase,
	 *      com.jdroid.android.usecase.listener.DefaultUseCaseListener,
	 *      com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger)
	 */
	@Override
	public void onResumeUseCase(final DefaultAbstractUseCase useCase, final DefaultUseCaseListener listener,
			final UseCaseTrigger useCaseTrigger) {
		if (useCase != null) {
			ExecutorUtils.execute(new Runnable() {
				
				@Override
				public void run() {
					useCase.addListener(listener);
					if (useCase.isNotified()) {
						if (useCaseTrigger.equals(UseCaseTrigger.ALWAYS)) {
							useCase.run();
						}
					} else {
						if (useCase.isInProgress()) {
							listener.onStartUseCase();
						} else if (useCase.isFinishSuccessful()) {
							listener.onFinishUseCase();
							useCase.markAsNotified();
						} else if (useCase.isFinishFailed()) {
							try {
								listener.onFinishFailedUseCase(useCase.getRuntimeException());
							} finally {
								useCase.markAsNotified();
							}
						} else if (useCase.isNotInvoked()
								&& (useCaseTrigger.equals(UseCaseTrigger.ONCE) || useCaseTrigger.equals(UseCaseTrigger.ALWAYS))) {
							useCase.run();
						}
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
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#onPauseUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase,
	 *      com.jdroid.android.usecase.listener.DefaultUseCaseListener)
	 */
	@Override
	public void onPauseUseCase(final DefaultAbstractUseCase userCase, final DefaultUseCaseListener listener) {
		if (userCase != null) {
			userCase.removeListener(listener);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeUseCase(com.jdroid.android.usecase.UseCase)
	 */
	@Override
	public void executeUseCase(UseCase<?> useCase) {
		ExecutorUtils.execute(useCase);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeUseCase(com.jdroid.android.usecase.UseCase, java.lang.Long)
	 */
	@Override
	public void executeUseCase(UseCase<?> useCase, Long delaySeconds) {
		ExecutorUtils.schedule(useCase, delaySeconds);
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		showLoading();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onUpdateUseCase()
	 */
	@Override
	public void onUpdateUseCase() {
		// Do nothing by default
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		dismissLoading();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(java.lang.RuntimeException)
	 */
	@Override
	public void onFinishFailedUseCase(RuntimeException runtimeException) {
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf != null) {
			if (fragmentIf.goBackOnError(runtimeException)) {
				DefaultExceptionHandler.markAsGoBackOnError(runtimeException);
			} else {
				DefaultExceptionHandler.markAsNotGoBackOnError(runtimeException);
			}
			dismissLoading();
		}
		throw runtimeException;
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishCanceledUseCase()
	 */
	@Override
	public void onFinishCanceledUseCase() {
		// Do nothing by default
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#goBackOnError(java.lang.RuntimeException)
	 */
	@Override
	public Boolean goBackOnError(RuntimeException runtimeException) {
		return true;
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#isBlockingLoadingEnabled()
	 */
	@Override
	public Boolean isBlockingLoadingEnabled() {
		return true;
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#isNonBlockingLoadingDisplayedByDefault()
	 */
	@Override
	public Boolean isNonBlockingLoadingDisplayedByDefault() {
		return true;
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
	 * @see com.jdroid.android.activity.ComponentIf#showBlockingLoading()
	 */
	@Override
	public void showBlockingLoading() {
		getActivityIf().showBlockingLoading();
		
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#showBlockingLoading(com.jdroid.android.loading.LoadingDialogBuilder)
	 */
	@Override
	public void showBlockingLoading(LoadingDialogBuilder builder) {
		getActivityIf().showBlockingLoading(builder);
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#dismissBlockingLoading()
	 */
	@Override
	public void dismissBlockingLoading() {
		getActivityIf().dismissBlockingLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showNonBlockingLoading()
	 */
	@Override
	public void showNonBlockingLoading() {
		loadingLayout.showLoading(getFragmentIf());
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissNonBlockingLoading()
	 */
	@Override
	public void dismissNonBlockingLoading() {
		loadingLayout.dismissLoading(getFragmentIf());
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading()
	 */
	@Override
	public void showLoading() {
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf != null) {
			if (fragmentIf.isBlockingLoadingEnabled()) {
				fragmentIf.showBlockingLoading();
			} else {
				fragmentIf.showNonBlockingLoading();
			}
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissLoading()
	 */
	@Override
	public void dismissLoading() {
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf != null) {
			if (fragmentIf.isBlockingLoadingEnabled()) {
				fragmentIf.dismissBlockingLoading();
			} else {
				fragmentIf.dismissNonBlockingLoading();
			}
		}
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#getAppContext()
	 */
	@Override
	public AppContext getAppContext() {
		return getActivityIf().getAppContext();
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return getActivityIf().getAdSize();
	}
	
	public OnClickListener getRemoveAdsClickListener() {
		return null;
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
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getActionBar()
	 */
	@Override
	public ActionBar getActionBar() {
		return fragment.getActivity().getActionBar();
	}
}
