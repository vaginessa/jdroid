package com.jdroid.android.social.googleplus;

import java.util.List;
import org.slf4j.Logger;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.social.AccountType;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.social.SocialUser;
import com.jdroid.android.utils.GooglePlayUtils;
import com.jdroid.android.utils.IntentUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.utils.LoggerUtils;

public class GooglePlusHelperFragment extends AbstractFragment implements ConnectionCallbacks,
		OnConnectionFailedListener {
	
	private static Logger LOGGER = LoggerUtils.getLogger(GooglePlusHelperFragment.class);
	
	private static final String RESOLVING_ERROR_KEY = "resolvingError";
	private static final String SIGN_IN_CLICKED_KEY = "signInClicked";
	private static final String SHARE_LINK_KEY = "shareLink";
	private static final String DIALOG_ERROR_KEY = "dialogErrorKey";
	
	// Request code to use when launching the resolution activity
	public static final int REQUEST_RESOLVE_ERROR = 1001;
	public static final int SHARE_REQUEST_CODE = 1002;
	
	private GoogleApiClient googleApiClient;
	private ConnectionResult connectionResult;
	private GooglePlusAuthenticationUseCase googlePlusAuthenticationUseCase;
	
	// Boolean to track whether the app is already resolving an error
	private boolean resolvingError = false;
	
	// Track whether the sign-in button has been clicked so that we know to resolve all issues preventing sign-in
	// without waiting.
	private boolean signInClicked = false;
	
	private String shareLink;
	
	public static void add(FragmentActivity activity,
			Class<? extends GooglePlusHelperFragment> googlePlusHelperFragmentClass, Fragment targetFragment) {
		add(activity, googlePlusHelperFragmentClass, null, targetFragment);
	}
	
	public static void add(FragmentActivity activity,
			Class<? extends GooglePlusHelperFragment> googlePlusHelperFragmentClass, Bundle bundle,
			Fragment targetFragment) {
		
		AbstractFragmentActivity abstractFragmentActivity = (AbstractFragmentActivity)activity;
		GooglePlusHelperFragment googlePlusHelperFragment = abstractFragmentActivity.instanceFragment(
			googlePlusHelperFragmentClass, bundle);
		googlePlusHelperFragment.setTargetFragment(targetFragment, 0);
		FragmentTransaction fragmentTransaction = abstractFragmentActivity.getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(0, googlePlusHelperFragment, GooglePlusHelperFragment.class.getSimpleName());
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
	
	public static GooglePlusHelperFragment get(FragmentActivity activity) {
		return ((AbstractFragmentActivity)activity).getFragment(GooglePlusHelperFragment.class);
	}
	
	public static void removeTarget(FragmentActivity activity) {
		GooglePlusHelperFragment googlePlusHelperFragment = GooglePlusHelperFragment.get(activity);
		if (googlePlusHelperFragment != null) {
			googlePlusHelperFragment.setTargetFragment(null, 0);
		}
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		if (savedInstanceState != null) {
			resolvingError = savedInstanceState.getBoolean(RESOLVING_ERROR_KEY);
			signInClicked = savedInstanceState.getBoolean(SIGN_IN_CLICKED_KEY);
			shareLink = savedInstanceState.getString(SHARE_LINK_KEY);
		}
		
		googlePlusAuthenticationUseCase = createGooglePlusAuthenticationUseCase();
		
		Builder builder = new GoogleApiClient.Builder(getActivity());
		builder.addApi(Plus.API);
		builder.addScope(Plus.SCOPE_PLUS_LOGIN);
		builder.addScope(Plus.SCOPE_PLUS_PROFILE);
		builder.addConnectionCallbacks(this);
		builder.addOnConnectionFailedListener(this);
		googleApiClient = builder.build();
	}
	
	protected GooglePlusAuthenticationUseCase createGooglePlusAuthenticationUseCase() {
		return null;
	}
	
	public GooglePlusAuthenticationUseCase getGooglePlusAuthenticationUseCase() {
		return googlePlusAuthenticationUseCase;
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		
		// This keeps track of the app state while the user is resolving the error to avoid repetitive attempts to
		// resolve the same error. For instance, while the account picker dialog is showing to resolve the
		// SIGN_IN_REQUIRED error, the user may rotate the screen. This recreates your activity and causes your
		// onStart() method to be called again, which then calls connect() again. This results in another call to
		// startResolutionForResult(), which creates another account picker dialog in front of the existing one.
		if (!resolvingError) {
			googleApiClient.connect();
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(googlePlusAuthenticationUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(googlePlusAuthenticationUseCase, this);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		// It can be a little costly to keep the connection open to Google Play Services, so each time our activity is
		// stopped we should disconnect.
		googleApiClient.disconnect();
		super.onStop();
	}
	
	private GooglePlusListener getGooglePlusListener() {
		return (GooglePlusListener)getTargetFragment();
	}
	
	/**
	 * @see com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener#onConnectionFailed(com.google.android.gms.common.ConnectionResult)
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (resolvingError) {
			// Already attempting to resolve an error.
			return;
		}
		
		this.connectionResult = connectionResult;
		if (signInClicked) {
			if (connectionResult.hasResolution()) {
				startResolution(connectionResult);
			} else {
				showErrorDialog(connectionResult.getErrorCode());
			}
		} else {
			GooglePlusListener googlePlusListener = getGooglePlusListener();
			if (googlePlusListener != null) {
				LOGGER.debug("Executing onGooglePlusConnectionFailed");
				googlePlusListener.onGooglePlusConnectionFailed();
			}
		}
	}
	
	/**
	 * A helper method to flip the resolvingError flag and start the resolution of the ConnenctionResult from the failed
	 * connect() call.
	 */
	private void startResolution(ConnectionResult connectionResult) {
		
		Boolean error = false;
		try {
			Activity activity = getActivity();
			if ((connectionResult != null) && (activity != null)) {
				// Don't start another resolution now until we have a result from the activity we're about to start.
				resolvingError = true;
				// If we can resolve the error, then call start resolution and pass it an integer tag we can use to
				// track. This means that when we get the onActivityResult callback we'll know its from being started
				// here.
				connectionResult.startResolutionForResult(activity, REQUEST_RESOLVE_ERROR);
			} else {
				error = true;
			}
		} catch (SendIntentException e) {
			error = true;
		}
		
		if (error) {
			// Any problems, just try to connect() again so we get a new ConnectionResult.
			resolvingError = false;
			googleApiClient.connect();
		}
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(RESOLVING_ERROR_KEY, resolvingError);
		outState.putBoolean(SIGN_IN_CLICKED_KEY, signInClicked);
		outState.putString(SHARE_LINK_KEY, shareLink);
	}
	
	/**
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onConnected(android.os.Bundle)
	 */
	@Override
	public void onConnected(Bundle bundle) {
		
		Person me = Plus.PeopleApi.getCurrentPerson(googleApiClient);
		if (signInClicked) {
			signInClicked = false;
			if (me == null) {
				GooglePlusListener googlePlusListener = getGooglePlusListener();
				if (googlePlusListener != null) {
					// TODO See this event
					LOGGER.debug("Executing onGooglePlusDisconnected");
					googlePlusListener.onGooglePlusDisconnected();
				}
			} else {
				googlePlusAuthenticationUseCase.setPerson(me);
				googlePlusAuthenticationUseCase.setAccount(Plus.AccountApi.getAccountName(googleApiClient));
				googlePlusAuthenticationUseCase.setLoginMode(true);
				executeUseCase(googlePlusAuthenticationUseCase);
			}
		} else {
			GooglePlusListener googlePlusListener = getGooglePlusListener();
			if ((googlePlusListener != null) && (me != null)) {
				LOGGER.debug("Executing onGooglePlusConnected");
				googlePlusListener.onGooglePlusConnected(me, Plus.AccountApi.getAccountName(googleApiClient));
			}
		}
	}
	
	/**
	 * @see com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks#onConnectionSuspended(int)
	 */
	@Override
	public void onConnectionSuspended(int cause) {
		GooglePlusListener googlePlusListener = getGooglePlusListener();
		if (googlePlusListener != null) {
			LOGGER.debug("Executing onGooglePlusDisconnected");
			googlePlusListener.onGooglePlusDisconnected();
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishFailedUseCase(com.jdroid.java.exception.AbstractException)
	 */
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		if (googleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(googleApiClient);
			googleApiClient.disconnect();
			googleApiClient.connect();
		}
		
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				GooglePlusListener googlePlusListener = getGooglePlusListener();
				if (googlePlusListener != null) {
					LOGGER.debug("Executing onGooglePlusSignInCanceled");
					googlePlusListener.onGooglePlusSignInFailed();
				}
			}
		});
		super.onFinishFailedUseCase(abstractException);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				GooglePlusListener googlePlusListener = getGooglePlusListener();
				if ((googlePlusListener != null) && googlePlusAuthenticationUseCase.isLoginMode()) {
					LOGGER.debug("Executing onGooglePlusSignIn");
					googlePlusListener.onGooglePlusSignIn(googlePlusAuthenticationUseCase.getPerson(),
						googlePlusAuthenticationUseCase.getAccount());
				}
				dismissLoading();
			}
		});
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_RESOLVE_ERROR) {
			resolvingError = false;
			if (resultCode == Activity.RESULT_OK) {
				// Make sure the app is not already connected or attempting to connect
				if (!googleApiClient.isConnecting() && !googleApiClient.isConnected()) {
					googleApiClient.connect();
				}
			} else {
				signInClicked = false;
				GooglePlusListener googlePlusListener = getGooglePlusListener();
				if (googlePlusListener != null) {
					LOGGER.debug("Executing onGooglePlusSignInFailed");
					googlePlusListener.onGooglePlusSignInFailed();
				}
			}
		} else if ((requestCode == SHARE_REQUEST_CODE) && (resultCode == Activity.RESULT_OK) && (shareLink != null)) {
			AbstractApplication.get().getAnalyticsSender().trackSocialInteraction(AccountType.GOOGLE_PLUS,
				SocialAction.SHARE, shareLink);
			shareLink = null;
		}
	}
	
	public void signIn() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
		if ((resultCode == ConnectionResult.SERVICE_MISSING)
				|| (resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED)
				|| (resultCode == ConnectionResult.SERVICE_DISABLED)
				|| (resultCode == ConnectionResult.SERVICE_INVALID)) {
			GooglePlusListener googlePlusListener = getGooglePlusListener();
			if (googlePlusListener != null) {
				LOGGER.debug("Executing onGooglePlusDisconnected");
				googlePlusListener.onGooglePlusDisconnected();
			}
			showErrorDialog(resultCode);
		} else {
			signInClicked = true;
			if (googleApiClient.isConnected()) {
				// If user is connected but press sign in we start connection flow.
				googleApiClient.disconnect();
				googleApiClient.connect();
			} else if (!googleApiClient.isConnecting()) {
				// We should always have a connection result ready to resolve, so we can start that process.
				if ((connectionResult != null) && connectionResult.hasResolution()) {
					startResolution(connectionResult);
				} else {
					// If we don't have one though, we can start connect in order to retrieve one.
					googleApiClient.connect();
				}
			}
		}
	}
	
	public void signOut() {
		// We only want to sign out if we're connected.
		if (googleApiClient.isConnected()) {
			// Clear the default account in order to allow the user to potentially choose a different account from the
			// account chooser.
			Plus.AccountApi.clearDefaultAccount(googleApiClient);
			googlePlusAuthenticationUseCase.setLoginMode(false);
			executeUseCase(googlePlusAuthenticationUseCase);
			
			// Disconnect from Google Play Services, then reconnect in order to restart the process from scratch.
			googleApiClient.disconnect();
			googleApiClient.connect();
		} else {
			GooglePlusListener googlePlusListener = getGooglePlusListener();
			if (googlePlusListener != null) {
				LOGGER.debug("Executing onGooglePlusSignOut");
				googlePlusListener.onGooglePlusSignOut();
			}
		}
		
	}
	
	public void loadPeople() {
		Plus.PeopleApi.loadVisible(googleApiClient, null).setResultCallback(
			new ResultCallback<People.LoadPeopleResult>() {
				
				@Override
				public void onResult(LoadPeopleResult loadPeopleResult) {
					if (loadPeopleResult.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
						
						PersonBuffer personBuffer = loadPeopleResult.getPersonBuffer();
						List<SocialUser> googlePlusUsers = Lists.newArrayList();
						try {
							int count = personBuffer.getCount();
							for (int i = 0; i < count; i++) {
								Person person = personBuffer.get(i);
								googlePlusUsers.add(new SocialUser(null, person.getId(), AccountType.GOOGLE_PLUS,
										person.getDisplayName(), "", person.getImage().getUrl()));
							}
							GooglePlusListener googlePlusListener = getGooglePlusListener();
							if (googlePlusListener != null) {
								LOGGER.debug("Executing onGooglePlusFriendsLoaded");
								googlePlusListener.onGooglePlusFriendsLoaded(googlePlusUsers);
							}
						} finally {
							personBuffer.close();
						}
					}
				}
			});
	}
	
	public void share(String content, String link) {
		PlusShare.Builder builder = new PlusShare.Builder(getActivity());
		builder.setText(content);
		builder.setType(MimeType.TEXT);
		builder.setContentUrl(Uri.parse(link));
		builder.setContentDeepLinkId(link);
		
		Intent intent = builder.getIntent();
		shareLink = link;
		if (IntentUtils.isIntentAvailable(intent)) {
			getActivity().startActivityForResult(intent, SHARE_REQUEST_CODE);
		} else {
			GooglePlayUtils.showDownloadDialog(R.string.googlePlus, "com.google.android.apps.plus");
		}
	}
	
	public static void signOut(Context context) {
		
		if (GooglePlayUtils.isGooglePlayServicesAvailable(context)) {
			
			LogoutListener logoutListener = new LogoutListener();
			
			Builder builder = new GoogleApiClient.Builder(context);
			builder.addApi(Plus.API);
			builder.addConnectionCallbacks(logoutListener);
			GoogleApiClient googleApiClient = builder.build();
			logoutListener.setGoogleApiClient(googleApiClient);
			googleApiClient.connect();
		}
	}
	
	public static void openCommunity(String community) {
		AbstractApplication.get().getCurrentActivity().startActivity(
			new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/communities/" + community)));
		AbstractApplication.get().getAnalyticsSender().trackSocialInteraction(AccountType.GOOGLE_PLUS,
			SocialAction.OPEN_PROFILE, community);
	}
	
	public static class LogoutListener implements ConnectionCallbacks {
		
		private GoogleApiClient googleApiClient;
		
		@Override
		public void onConnected(Bundle arg0) {
			Plus.AccountApi.clearDefaultAccount(googleApiClient);
		}
		
		@Override
		public void onConnectionSuspended(int arg0) {
		}
		
		public void setGoogleApiClient(GoogleApiClient googleApiClient) {
			this.googleApiClient = googleApiClient;
		}
	}
	
	public void revokeAccess() {
		
		if (googleApiClient.isConnected()) {
			// Clear the default account as in the Sign Out.
			Plus.AccountApi.clearDefaultAccount(googleApiClient);
			Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient).setResultCallback(new ResultCallback<Status>() {
				
				@Override
				public void onResult(Status status) {
					googlePlusAuthenticationUseCase.setLoginMode(false);
					executeUseCase(googlePlusAuthenticationUseCase);
					
					googleApiClient.connect();
					
					GooglePlusListener googlePlusListener = getGooglePlusListener();
					if (googlePlusListener != null) {
						LOGGER.debug("Executing onGooglePlusAccessRevoked");
						googlePlusListener.onGooglePlusAccessRevoked();
					}
				}
			});
		} else {
			googleApiClient.connect();
			
			// This case is when an user is logged in google+ and we remove the access from the phone.
			GooglePlusListener googlePlusListener = getGooglePlusListener();
			if (googlePlusListener != null) {
				LOGGER.debug("Executing onGooglePlusAccessRevoked");
				googlePlusListener.onGooglePlusAccessRevoked();
			}
		}
	}
	
	/**
	 * Creates a dialog for an error message
	 * 
	 * @param errorCode
	 */
	private void showErrorDialog(int errorCode) {
		
		resolvingError = true;
		
		// Create a fragment for the error dialog
		ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
		// Pass the error that should be displayed
		Bundle args = new Bundle();
		args.putInt(DIALOG_ERROR_KEY, errorCode);
		dialogFragment.setArguments(args);
		dialogFragment.setTargetFragment(this, 0);
		
		Fragment currentErrorDialogFragment = getActivity().getSupportFragmentManager().findFragmentByTag("a");
		if (currentErrorDialogFragment == null) {
			dialogFragment.show(getActivity().getSupportFragmentManager(), "a");
		}
	}
	
	/**
	 * Called from ErrorDialogFragment when the dialog is dismissed
	 */
	public void onDialogDismissed() {
		resolvingError = false;
	}
	
	public static class ErrorDialogFragment extends DialogFragment {
		
		public ErrorDialogFragment() {
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Get the error code and retrieve the appropriate dialog
			int errorCode = this.getArguments().getInt(DIALOG_ERROR_KEY);
			return GooglePlayServicesUtil.getErrorDialog(errorCode, this.getActivity(), REQUEST_RESOLVE_ERROR);
		}
		
		/**
		 * @see android.support.v4.app.DialogFragment#onDismiss(android.content.DialogInterface)
		 */
		@Override
		public void onDismiss(DialogInterface dialog) {
			super.onDismiss(dialog);
			Fragment targetFragment = getTargetFragment();
			if (targetFragment != null) {
				((GooglePlusHelperFragment)targetFragment).onDialogDismissed();
			}
		}
	}
}
