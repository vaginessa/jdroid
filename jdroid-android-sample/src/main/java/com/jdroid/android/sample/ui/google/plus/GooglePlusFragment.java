package com.jdroid.android.sample.ui.google.plus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.plus.GooglePlusHelperFragment;
import com.jdroid.android.google.plus.GooglePlusListener;
import com.jdroid.android.sample.R;
import com.jdroid.android.social.SocialUser;

import java.util.List;

public class GooglePlusFragment extends AbstractFragment implements GooglePlusListener {

	private TextView status;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			GooglePlusHelperFragment.add(getActivity(), LoginGooglePlusHelperFragment.class, this);
		}
	}

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.google_plus_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		status = findView(R.id.status);

		findView(R.id.googlePlusSignInButton).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				GooglePlusHelperFragment.get(getActivity()).signIn();
			}
		});

		findView(R.id.googlePlusLogoutButton).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				GooglePlusHelperFragment.get(getActivity()).signOut();
			}
		});

		findView(R.id.googlePlusRevokeButton).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				GooglePlusHelperFragment.get(getActivity()).revokeAccess();
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		GooglePlusHelperFragment googlePlusHelperFragment = GooglePlusHelperFragment.get(getActivity());
		if (googlePlusHelperFragment != null) {
			googlePlusHelperFragment.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		GooglePlusHelperFragment googlePlusHelperFragment = GooglePlusHelperFragment.get(getActivity());
		if (googlePlusHelperFragment != null) {
			googlePlusHelperFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		GooglePlusHelperFragment.removeTarget(getActivity());
	}


	@Override
	public void onGooglePlusSignInFailed() {
		status.setText("Not Logged");
		Snackbar.make(findView(R.id.container), "onGooglePlusSignInFailed", Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public void onGooglePlusAccessRevoked() {
		status.setText("Not Logged");
		Snackbar.make(findView(R.id.container), "onGooglePlusAccessRevoked", Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public void onGooglePlusSignOut() {
		status.setText("Not Logged");
		Snackbar.make(findView(R.id.container), "onGooglePlusSignOut", Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public void onGooglePlusConnectionFailed() {
		status.setText("Not Logged");
		Snackbar.make(findView(R.id.container), "onGooglePlusConnectionFailed", Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public void onGooglePlusSignIn(Person me, String accountName) {
		status.setText("Logged as " + accountName);
		Snackbar.make(findView(R.id.container), "onGooglePlusSignIn", Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public void onGooglePlusConnected(Person me, String accountName) {
		status.setText("Logged as " + accountName);
		Snackbar.make(findView(R.id.container), "onGooglePlusConnected", Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public void onGooglePlusDisconnected() {
		status.setText("Not Logged");
		Snackbar.make(findView(R.id.container), "onGooglePlusDisconnected", Snackbar.LENGTH_SHORT).show();
	}

	@Override
	public void onGooglePlusFriendsLoaded(List<SocialUser> friends) {
		Snackbar.make(findView(R.id.container), "onGooglePlusFriendsLoaded", Snackbar.LENGTH_SHORT).show();
	}
}
