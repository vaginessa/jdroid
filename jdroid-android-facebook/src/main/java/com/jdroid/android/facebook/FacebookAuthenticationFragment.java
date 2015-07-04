package com.jdroid.android.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.FeedDialogBuilder;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.social.AccountType;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.utils.ExternalAppsUtils;
import com.jdroid.android.utils.GooglePlayUtils;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class FacebookAuthenticationFragment<T extends FacebookAuthenticationUseCase> extends AbstractFragment implements
		SessionStateListener, FacebookAuthenticationListener {
	
	private final Logger logger = LoggerUtils.getLogger(getClass());
	
	private FacebookConnector facebookConnector;
	private T facebookAuthenticationUseCase;
	
	public static void add(FragmentActivity activity,
			Class<? extends FacebookAuthenticationFragment<?>> facebookAuthenticationFragmentClass,
			Fragment targetFragment) {
		add(activity, facebookAuthenticationFragmentClass, null, targetFragment);
	}
	
	public static void add(FragmentActivity activity,
			Class<? extends FacebookAuthenticationFragment<?>> facebookAuthenticationFragmentClass, Bundle bundle,
			Fragment targetFragment) {
		
		AbstractFragmentActivity abstractFragmentActivity = (AbstractFragmentActivity)activity;
		FacebookAuthenticationFragment<?> facebookAuthenticationFragment = abstractFragmentActivity.instanceFragment(
			facebookAuthenticationFragmentClass != null ? facebookAuthenticationFragmentClass
					: FacebookAuthenticationFragment.class, bundle);
		facebookAuthenticationFragment.setTargetFragment(targetFragment, 0);
		FragmentTransaction fragmentTransaction = abstractFragmentActivity.getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(0, facebookAuthenticationFragment, FacebookAuthenticationFragment.class.getSimpleName());
		fragmentTransaction.commit();
	}
	
	public static void remove(FragmentActivity activity) {
		Fragment fragmentToRemove = get(activity);
		if (fragmentToRemove != null) {
			FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
			fragmentTransaction.remove(fragmentToRemove);
			fragmentTransaction.commit();
		}
	}
	
	public static FacebookAuthenticationFragment<?> get(FragmentActivity activity) {
		return ((AbstractFragmentActivity)activity).getFragment(FacebookAuthenticationFragment.class);
	}
	
	public static void removeTarget(FragmentActivity activity) {
		FacebookAuthenticationFragment<?> facebookAuthenticationFragment = FacebookAuthenticationFragment.get(activity);
		if (facebookAuthenticationFragment != null) {
			facebookAuthenticationFragment.setTargetFragment(null, 0);
		}
	}
	
	private FacebookListener getFacebookListener() {
		return (FacebookListener)getTargetFragment();
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		facebookAuthenticationUseCase = createFacebookAuthenticationUseCase();
		facebookConnector = new FacebookConnector(getActivity(), this, this);
		facebookConnector.onCreate(savedInstanceState);
	}
	
	@SuppressWarnings("unchecked")
	protected T createFacebookAuthenticationUseCase() {
		return (T)new FacebookAuthenticationUseCase();
	}
	
	public T getFacebookAuthenticationUseCase() {
		return facebookAuthenticationUseCase;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(facebookAuthenticationUseCase, this);
		facebookConnector.onResume();
		
		verifyFacebookConnection();
	}
	
	private void verifyFacebookConnection() {
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				final Boolean isConnected = FacebookPreferencesUtils.verifyFacebookAccessToken();
				executeOnUIThread(new Runnable() {
					
					@Override
					public void run() {
						FacebookListener facebookListener = getFacebookListener();
						if (facebookListener != null) {
							if (isConnected) {
								facebookListener.onFacebookConnected(FacebookPreferencesUtils.loadFacebookUser());
							} else {
								facebookListener.onFacebookDisconnected();
							}
						}
					}
				});
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(facebookAuthenticationUseCase, this);
		facebookConnector.onPause();
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		facebookConnector.onDestroy();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebookConnector.onActivityResult(requestCode, resultCode, data);
	}
	
	protected Logger getLogger() {
		return logger;
	}
	
	public void startLoginProcess() {
		facebookConnector.login(this);
	}
	
	public void startLoginForPublishProcess() {
		facebookConnector.loginForPublish(this);
	}
	
	public void startLogoutProcess() {
		facebookConnector.logout();
	}
	
	public void share(String name, String caption, String description, final String link, String picture) {
		if (ExternalAppsUtils.isAppInstalled(getActivity(), ExternalAppsUtils.FACEBOOK_PACKAGE_NAME)) {
			Session session = Session.getActiveSession();
			if ((session != null) && session.isOpened()) {
				
				Bundle params = new Bundle();
				params.putString("name", name);
				params.putString("caption", caption);
				params.putString("description", description);
				params.putString("link", link);
				params.putString("picture", picture);
				
				// Invoke the dialog
				FeedDialogBuilder builder = new WebDialog.FeedDialogBuilder(getActivity(), Session.getActiveSession(),
						params);
				builder.setOnCompleteListener(new OnCompleteListener() {
					
					@Override
					public void onComplete(Bundle values, FacebookException facebookException) {
						if ((facebookException != null)
								&& (facebookException.getClass() != FacebookOperationCanceledException.class)) {
							ToastUtils.showToast(R.string.sharingFailed);
						} else {
							AbstractApplication.get().getAnalyticsSender().trackSocialInteraction(AccountType.FACEBOOK,
								SocialAction.SHARE, link);
						}
					}
				});
				builder.build().show();
			} else {
				startLoginProcess();
			}
		} else {
			GooglePlayUtils.showDownloadDialog(R.string.facebook, ExternalAppsUtils.FACEBOOK_PACKAGE_NAME);
		}
	}

	/**
	 * @see FacebookAuthenticationListener#onFacebookLoginCompleted(FacebookConnector)
	 */
	@Override
	public void onFacebookLoginCompleted(FacebookConnector facebookConnector) {
		facebookAuthenticationUseCase.setLoginMode(true);
		facebookAuthenticationUseCase.setFacebookConnector(facebookConnector);
		executeUseCase(facebookAuthenticationUseCase);
	}
	
	/**
	 * @see FacebookAuthenticationListener#onFacebookLogoutCompleted()
	 */
	@Override
	public void onFacebookLogoutCompleted() {
		facebookAuthenticationUseCase.setLoginMode(false);
		facebookAuthenticationUseCase.setFacebookConnector(facebookConnector);
		executeUseCase(facebookAuthenticationUseCase);
	}
	
	// The onStartUseCase is overridden here to use the executeOnUIThread implementation
	// for fragment that uses internally SafeExecuteWrapperRunnable to check if the fragment
	// is in the resumed state. This is to fix an issue detected returning from Facebook Login,
	// when "Don't keep activities" option is turned on, in this case the loading never dismissed.
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		this.executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				showLoading();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				FacebookListener facebookListener = getFacebookListener();
				if (facebookListener != null) {
					if (facebookAuthenticationUseCase.isLoginMode()) {
						getFacebookListener().onFacebookSignIn(facebookAuthenticationUseCase.getFacebookUser());
					} else {
						getFacebookListener().onFacebookDisconnected();
					}
				}
				dismissLoading();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishFailedUseCase(com.jdroid.java.exception.AbstractException)
	 */
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		facebookConnector.localLogout();
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				FacebookListener facebookListener = getFacebookListener();
				if (facebookListener != null) {
					facebookListener.onFacebookSignInFailed();
				}
			}
		});
		super.onFinishFailedUseCase(abstractException);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#goBackOnError(com.jdroid.java.exception.AbstractException)
	 */
	@Override
	public Boolean goBackOnError(AbstractException abstractException) {
		return false;
	}
	
	/**
	 * @see SessionStateListener#onSessionOpened()
	 */
	@Override
	public void onSessionOpened() {
		logger.debug("onSessionOpened()");
	}
	
	/**
	 * @see SessionStateListener#onSessionClosed()
	 */
	@Override
	public void onSessionClosed() {
		logger.debug("onSessionClosed()");
	}
	
	/**
	 * @see SessionStateListener#onSessionOpenedWithUpdatedToken()
	 */
	@Override
	public void onSessionOpenedWithUpdatedToken() {
		logger.debug("onSessionOpenedWithUpdatedToken()");
	}
	
	/**
	 * @see SessionStateListener#onSessionClosedLoginFailed()
	 */
	@Override
	public void onSessionClosedLoginFailed() {
		logger.debug("onSessionClosedLoginFailed()");
	}
	
}
