package com.jdroid.android.google.maps;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.GooglePlayServicesUtils;

public class MapFacadeFragment extends AbstractFragment {

	private static final String MAP_FRAGMENT_CLASS = "mapFragmentClass";
	public static final String FRAGMENT_CONTAINER_ID = "fragmentContainerId";

	private Class<? extends AbstractMapFragment> abstractMapFragmentClass;
	private int fragmentContainerId;

	private TextView legend;
	private Button button;

	public static Fragment instanceMapFragment(AbstractFragmentActivity abstractFragmentActivity, @LayoutRes int fragmentContainerId, Class<? extends AbstractMapFragment> abstractMapFragmentClass, Bundle bundle) {
		if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(abstractFragmentActivity)) {
			return abstractFragmentActivity.instanceFragment(abstractMapFragmentClass, bundle);
		} else {
			if (bundle == null) {
				bundle = new Bundle();
			}
			bundle.putSerializable(MAP_FRAGMENT_CLASS, abstractMapFragmentClass);
			bundle.putSerializable(FRAGMENT_CONTAINER_ID, fragmentContainerId);
			return abstractFragmentActivity.instanceFragment(MapFacadeFragment.class, bundle);
		}
	}

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.map_facade_fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		abstractMapFragmentClass = getArgument(MAP_FRAGMENT_CLASS);
		fragmentContainerId = getArgument(FRAGMENT_CONTAINER_ID);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		legend = findView(R.id.legend);
		button = findView(R.id.button);
	}

	@Override
	public void onResume() {
		super.onResume();

		if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(getActivity())) {
			displayMap();
		} else {
			showUpdateGooglePlayServices();
		}
	}

	private void displayMap() {
		AbstractFragmentActivity abstractFragmentActivity = (AbstractFragmentActivity)getActivity();
		Fragment mapFragment = abstractFragmentActivity.instanceFragment(abstractMapFragmentClass, getArguments());

		FragmentTransaction fragmentTransaction = abstractFragmentActivity.getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(fragmentContainerId, mapFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}

	private void showUpdateGooglePlayServices() {

		legend.setVisibility(View.VISIBLE);

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				GooglePlayServicesUtils.launchGooglePlayServicesUpdate(getActivity());
			}
		});
		button.setVisibility(View.VISIBLE);
	}

}
