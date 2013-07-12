package com.jdroid.android.facebook;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.SharedPreferencesTokenCachingStrategy;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.jdroid.android.exception.CommonErrorCode;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.java.utils.LoggerUtils;

/**
 * This class handles all Facebook session events and requests.
 * <p>
 * When using this class, clients MUST call all the public methods from the respective methods in either an Activity or
 * Fragment. Failure to call all the methods can result in improperly initialized or uninitialized Sessions.
 */
public class FacebookConnector {
	
	private static final List<String> PERMISSIONS = Arrays.asList("email");
	private static final SessionLoginBehavior LOGIN_BEHAVIOR = SessionLoginBehavior.SSO_WITH_FALLBACK;
	private SessionDefaultAudience DEFAULT_AUDIENCE = SessionDefaultAudience.FRIENDS;
	
	private UiLifecycleHelper uiHelper;
	private SessionStateListener sessionStateListener;
	private String facebookAppId;
	private AbstractFragment fragment;
	private final Logger logger;
	private FacebookLoginListener facebookLoginListener;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	
	/**
	 * Creates a new {@link FacebookConnector}, building and activating a {@link Session}.
	 * 
	 * @param fragment the target {@link Fragment}. It is used to pause and resume the {@link FacebookRequestMeUseCase}
	 *            the {@link FacebookConnector} uses internally to get the {@link GraphUser}.
	 * @param facebookAppId the id of the Facebook application.
	 * @param sessionStateListener is a callback for session events.
	 * @param facebookLoginListener is a callback for Facebook login event.
	 * @return an initialized {@link FacebookConnector}.
	 */
	public static FacebookConnector instance(AbstractFragment fragment, String facebookAppId,
			SessionStateListener sessionStateListener, FacebookLoginListener facebookLoginListener) {
		buildSession(fragment, facebookAppId);
		return new FacebookConnector(fragment, facebookAppId, sessionStateListener, facebookLoginListener);
	}
	
	private static Session buildSession(AbstractFragment fragment, String facebookAppId) {
		SharedPreferencesTokenCachingStrategy tokenCachingStrategy = new SharedPreferencesTokenCachingStrategy(
				fragment.getActivity());
		Builder sessionBuilder = new Builder(fragment.getActivity());
		sessionBuilder.setTokenCachingStrategy(tokenCachingStrategy);
		sessionBuilder.setApplicationId(facebookAppId);
		Session session = sessionBuilder.build();
		Session.setActiveSession(session);
		
		return session;
	}
	
	private FacebookConnector(AbstractFragment fragment, String facebookAppId,
			SessionStateListener sessionStateListener, FacebookLoginListener facebookLoginListener) {
		this.facebookAppId = facebookAppId;
		this.sessionStateListener = sessionStateListener;
		this.fragment = fragment;
		logger = LoggerUtils.getLogger(fragment.getClass());
		uiHelper = new UiLifecycleHelper(fragment.getActivity(), callback);
		this.facebookLoginListener = facebookLoginListener;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		uiHelper.onCreate(savedInstanceState);
	}
	
	public void onResume() {
		uiHelper.onResume();
	}
	
	public void onPause() {
		uiHelper.onPause();
	}
	
	public void onDestroy() {
		uiHelper.onDestroy();
	}
	
	public void onSaveInstanceState(Bundle outState) {
		uiHelper.onSaveInstanceState(outState);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		uiHelper.onActivityResult(requestCode, resultCode, data);
		if ((requestCode == Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE) && (resultCode == Activity.RESULT_OK)) {
			notifyFacebookLoginListener();
		}
	}
	
	/**
	 * Initializes {@link LoginButton} with email permissions.
	 * 
	 * @param fragment
	 * @param facebookLoginButton
	 */
	public void initLoginButtonWithEmailPermissions(Fragment fragment, LoginButton facebookLoginButton) {
		facebookLoginButton.setFragment(fragment);
		facebookLoginButton.setReadPermissions(PERMISSIONS);
		facebookLoginButton.setApplicationId(facebookAppId);
	}
	
	/**
	 * 
	 * @param session FB's {@link Session}
	 * @param state The new state for FB's {@link Session}.
	 * @param exception The exception that may have been thrown when trying to change the {@link Session}'s state.
	 */
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		logger.debug("Facebook session state changed to " + state.name());
		
		if (sessionStateListener != null) {
			if (SessionState.OPENED_TOKEN_UPDATED.equals(state)) {
				sessionStateListener.onSessionOpenedWithUpdatedToken();
			} else if (SessionState.CLOSED.equals(state)) {
				sessionStateListener.onSessionClosed();
			} else if (SessionState.OPENED.equals(state)) {
				sessionStateListener.onSessionOpened();
			} else if (SessionState.CLOSED_LOGIN_FAILED.equals(state)) {
				sessionStateListener.onSessionClosedLoginFailed();
			}
		}
		
	}
	
	public void login() {
		Session currentSession = getCurrentSession();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Facebook currentSession currentSession.isOpened()=" + currentSession.isOpened());
			logger.debug("Facebook currentSession currentSession.isClosed()=" + currentSession.isClosed());
			logger.debug("Facebook currentSession currentSession state=" + currentSession.getState().name());
		}
		
		if (currentSession.isClosed()) {
			// Discard the current session and build a new one to avoid an exception
			Session.setActiveSession(null);
			currentSession = getCurrentSession();
		}
		
		if (currentSession.isOpened()) {
			notifyFacebookLoginListener();
		} else {
			Session.OpenRequest openRequest = new Session.OpenRequest(fragment);
			
			openRequest.setDefaultAudience(DEFAULT_AUDIENCE);
			openRequest.setPermissions(PERMISSIONS);
			openRequest.setLoginBehavior(LOGIN_BEHAVIOR);
			
			currentSession.openForRead(openRequest);
		}
	}
	
	public void logout() {
		Session currentSession = getCurrentSession();
		currentSession.closeAndClearTokenInformation();
	}
	
	public String getAccessToken() {
		return getCurrentSession().getAccessToken();
	}
	
	/**
	 * This method execute a Facebook "MeRequest" to get user profile info for the current user. This method should not
	 * be called from the UI thread.
	 * 
	 * @return GraphUser for the logged user
	 */
	public GraphUser executeMeRequest() {
		try {
			Response response = Request.executeAndWait(Request.newMeRequest(Session.getActiveSession(), null));
			return response.getGraphObjectAs(GraphUser.class);
		} catch (Exception e) {
			throw CommonErrorCode.SERVER_ERROR.newApplicationException(e);
		}
	}
	
	private Session getCurrentSession() {
		Session currentSession = Session.getActiveSession();
		if (currentSession == null) {
			currentSession = buildSession(fragment, facebookAppId);
		}
		return currentSession;
	}
	
	private void notifyFacebookLoginListener() {
		if (facebookLoginListener != null) {
			facebookLoginListener.onFacebookLoginCompleted(this);
		}
	}
}
