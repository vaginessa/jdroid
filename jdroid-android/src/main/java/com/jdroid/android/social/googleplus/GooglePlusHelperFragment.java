package com.jdroid.android.social.googleplus;

import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;
import com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.jdroid.android.social.googleplus.SocialUser.SocialNetwork;
import com.jdroid.android.utils.GooglePlayUtils;
import com.jdroid.java.collections.Lists;

/**
 * 
 * @author Maxi Rosson
 */
public class GooglePlusHelperFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener,
		OnAccessRevokedListener, OnPeopleLoadedListener {
	
	private static final String GOOGLE_PLUS_TAG = "googlePlusTag";
	private static final int REQUEST_CODE_SIGN_IN = 1;
	
	public static GooglePlusHelperFragment start(FragmentActivity activity, Fragment targetFragment) {
		
		GooglePlusHelperFragment fragment = null;
		if (GooglePlayUtils.isGooglePlayServicesAvailable(activity)) {
			// Check if the fragment is already attached.
			FragmentManager fragmentManager = activity.getSupportFragmentManager();
			fragment = (GooglePlusHelperFragment)fragmentManager.findFragmentByTag(GOOGLE_PLUS_TAG);
			// The fragment is attached. If it has the right visible activities, return it.
			if (fragment == null) {
				
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				
				// Create a new fragment and attach it to the fragment manager.
				fragment = new GooglePlusHelperFragment();
				fragment.setTargetFragment(targetFragment, 0);
				fragmentTransaction.add(fragment, GOOGLE_PLUS_TAG);
				fragmentTransaction.commit();
			}
		}
		return fragment;
	}
	
	private PlusClient plusClient;
	private ConnectionResult connectionResult;
	
	/**
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		plusClient = new PlusClient.Builder(getActivity(), this, this).build();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		plusClient.connect();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		plusClient.disconnect();
		super.onStop();
	}
	
	private GooglePlusListener getGooglePlusListener() {
		return (GooglePlusListener)getTargetFragment();
	}
	
	/**
	 * @see com.google.android.gms.plus.PlusClient.OnAccessRevokedListener#onAccessRevoked(com.google.android.gms.common.ConnectionResult)
	 */
	@Override
	public void onAccessRevoked(ConnectionResult connectionResult) {
		plusClient.connect();
		getGooglePlusListener().onAccessRevoked();
	}
	
	/**
	 * @see com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener#onConnectionFailed(com.google.android.gms.common.ConnectionResult)
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		this.connectionResult = connectionResult;
		getGooglePlusListener().onConnectionFailed();
	}
	
	/**
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onConnected(android.os.Bundle)
	 */
	@Override
	public void onConnected(Bundle bundle) {
		getGooglePlusListener().onConnected();
	}
	
	/**
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onDisconnected()
	 */
	@Override
	public void onDisconnected() {
		plusClient.connect();
		getGooglePlusListener().onDisconnected();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_SIGN_IN) {
			if ((resultCode == Activity.RESULT_OK) && !plusClient.isConnected() && !plusClient.isConnecting()) {
				// This time, connect should succeed.
				plusClient.connect();
			}
		}
	}
	
	/**
	 * @see com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener#onPeopleLoaded(com.google.android.gms.common.ConnectionResult,
	 *      com.google.android.gms.plus.model.people.PersonBuffer, java.lang.String)
	 */
	@Override
	public void onPeopleLoaded(ConnectionResult status, PersonBuffer personBuffer, String nextPageToken) {
		if (status.getErrorCode() == ConnectionResult.SUCCESS) {
			
			List<SocialUser> googlePlusUsers = Lists.newArrayList();
			try {
				int count = personBuffer.getCount();
				for (int i = 0; i < count; i++) {
					Person person = personBuffer.get(i);
					// if (person.isHasApp()) {
					googlePlusUsers.add(new SocialUser(null, SocialNetwork.GOOGLE_PLUS, person.getDisplayName(), "",
							person.getImage().getUrl()));
					// }
				}
				getGooglePlusListener().onPeopleLoaded(googlePlusUsers);
			} finally {
				personBuffer.close();
			}
		}
	}
	
	public void signIn() {
		try {
			connectionResult.startResolutionForResult(getActivity(), REQUEST_CODE_SIGN_IN);
		} catch (IntentSender.SendIntentException e) {
			// Fetch a new result to start.
			plusClient.connect();
		}
	}
	
	public void signOut() {
		if (plusClient.isConnected()) {
			plusClient.clearDefaultAccount();
			plusClient.disconnect();
			plusClient.connect();
		}
	}
	
	public void revokeAccess() {
		if (plusClient.isConnected()) {
			plusClient.revokeAccessAndDisconnect(this);
		}
	}
	
	public void loadPeople() {
		// TODO See these parameters
		plusClient.loadPeople(this, Person.Collection.VISIBLE, Person.OrderBy.ALPHABETICAL, 100, null);
	}
}
