package com.jdroid.android.google.maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.android.permission.PermissionHelper;
import com.jdroid.android.utils.AndroidUtils;

public class MapFacadeFragment extends AbstractFragment {

	private static final int STORAGE_PERMISSION_REQUEST_CODE = 1;

	private static final String MAP_FRAGMENT_CLASS = "mapFragmentClass";
	public static final String FRAGMENT_CONTAINER_ID = "fragmentContainerId";

	private Class<? extends AbstractMapFragment> abstractMapFragmentClass;
	private int fragmentContainerId;
	private Boolean storagePermissionGranted;

	private TextView legend;
	private Button button;

	public static Fragment instanceMapFragment(AbstractFragmentActivity abstractFragmentActivity, @LayoutRes int fragmentContainerId, Class<? extends AbstractMapFragment> abstractMapFragmentClass, Bundle bundle) {
		if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(abstractFragmentActivity) && PermissionHelper.verifyPermission(abstractFragmentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
			if (storagePermissionGranted == null) {
				storagePermissionGranted = PermissionHelper.checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_REQUEST_CODE);
			} else {
				storagePermissionGranted = PermissionHelper.verifyPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
				if (storagePermissionGranted) {
					displayMap();
				} else {
					showPermissionRequiredView();
				}
			}
		} else {
			showUpdateGooglePlayServices();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case STORAGE_PERMISSION_REQUEST_CODE: {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					storagePermissionGranted = true;
					displayMap();
				} else {
					storagePermissionGranted = false;
					showPermissionRequiredView();
				}
				return;
			}
		}
	}

	private void displayMap() {
		AbstractFragmentActivity abstractFragmentActivity = (AbstractFragmentActivity)getActivity();
		Fragment mapFragment = abstractFragmentActivity.instanceFragment(abstractMapFragmentClass, getArguments());

		FragmentTransaction fragmentTransaction = abstractFragmentActivity.getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(fragmentContainerId, mapFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}

	private void showPermissionRequiredView() {

		legend.setText(R.string.mapsRequiredPermissionLegend);
		legend.setVisibility(View.VISIBLE);

		if (PermissionHelper.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					PermissionHelper.requestPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_REQUEST_CODE);
				}
			});
		} else {
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Uri packageURI = Uri.parse("package:" + AndroidUtils.getApplicationId());
					Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
					getActivity().startActivity(intent);
				}
			});
		}

		button.setText(R.string.allow);
		button.setVisibility(View.VISIBLE);
	}

	private void showUpdateGooglePlayServices() {

		legend.setText(R.string.updateGooglePlayServices);
		legend.setVisibility(View.VISIBLE);

		button.setText(R.string.update);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				GooglePlayServicesUtils.launchGooglePlayServicesUpdate(getActivity());
			}
		});
		button.setVisibility(View.VISIBLE);
	}

}
