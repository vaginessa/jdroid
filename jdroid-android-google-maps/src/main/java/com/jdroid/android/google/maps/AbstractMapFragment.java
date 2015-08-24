package com.jdroid.android.google.maps;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.ad.HouseAdBuilder;
import com.jdroid.android.animation.FadeOutAnimation;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.fragment.FragmentIf;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.location.LocationHelper;
import com.jdroid.android.permission.PermissionHelper;
import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.android.usecase.UseCase;
import com.jdroid.android.usecase.listener.DefaultUseCaseListener;
import com.jdroid.java.exception.AbstractException;

public abstract class AbstractMapFragment extends SupportMapFragment implements FragmentIf, OnMyLocationChangeListener {
	
	private static final int DELAY_MILLI = 500;
	private static final int PERMISSION_REQUEST_CODE = 1;
	
	private FragmentHelper fragmentHelper;
	private GoogleMap map;
	private View mapCover;
	private ViewGroup mapContainer;
	private ViewGroup mapLegend;

	private Boolean permissionGranted = false;
	
	protected FragmentIf getFragmentIf() {
		return (FragmentIf)this.getActivity();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		// FIXME This is just a workaround to the following error: ClassNotFoundException when unmarshalling android.support.v7.widget.Toolbar$SavedState
		// It seems to be a problem with the SupportMapFragment implementation
		if (savedInstanceState != null) {
			SparseArray sparseArray = (SparseArray)savedInstanceState.get("android:view_state");
			if (sparseArray != null) {
				Integer keyToRemove = null;
				for (int i = 0; i < sparseArray.size(); i++) {
					int key = sparseArray.keyAt(i);
					// get the object by the key.
					Object each = sparseArray.get(key);
					if (each.toString().startsWith("android.support.v7.widget.Toolbar$SavedState")) {
						keyToRemove = key;
					}
				}

				if (keyToRemove != null) {
					sparseArray.remove(keyToRemove);
				}
			}
		}


		super.onCreate(savedInstanceState);
		fragmentHelper = AbstractApplication.get().createFragmentHelper(this);
		fragmentHelper.onCreate(savedInstanceState);

		checkPermission();
	}

	private void checkPermission() {
		permissionGranted = PermissionHelper.checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_CODE: {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					permissionGranted = true;
					setUpMap();
				} else {
					permissionGranted = false;
					showPermissionRequiredView();
				}
				return;
			}
		}
	}

	private void showPermissionRequiredView() {

		View permissionRequiredView = inflate(R.layout.permission_required);
		((TextView)permissionRequiredView.findViewById(R.id.permissionRequiredLegend)).setText(R.string.mapsRequiredPermissionLegend);

		View permissionRequiredButton = permissionRequiredView.findViewById(R.id.permissionRequiredButton);
		if (PermissionHelper.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			permissionRequiredButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					PermissionHelper.requestPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE);
				}
			});
			permissionRequiredButton.setVisibility(View.VISIBLE);
		} else {
			permissionRequiredButton.setVisibility(View.GONE);
		}

		mapLegend.removeAllViews();
		mapLegend.addView(permissionRequiredView);
		mapLegend.setVisibility(View.VISIBLE);
	}

	private void showUpdateGooglePlayServices() {
		View updateGooglePlayServicesView = inflate(R.layout.update_google_services);
		((TextView)updateGooglePlayServicesView.findViewById(R.id.updateGooglePlayServicesLegend)).setText(R.string.updateGooglePlayServices);
		updateGooglePlayServicesView.findViewById(R.id.updateGooglePlayServicesButton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GooglePlayServicesUtils.launchGooglePlayServicesUpdate(getActivity());
				getActivity().finish();
			}
		});

		mapLegend.removeAllViews();
		mapLegend.addView(updateGooglePlayServicesView);
		mapLegend.setVisibility(View.VISIBLE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = fragmentHelper.onCreateView(inflater, container, savedInstanceState);
		mapContainer = (ViewGroup)view.findViewById(R.id.mapContainer);

		if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(getActivity())) {

			View mapView = super.onCreateView(inflater, mapContainer, savedInstanceState);

			if (map == null) {
				mapView.setBackgroundResource(android.R.color.white);
				mapCover = view.findViewById(R.id.mapCover);
				TypedArray attributes = getActivity().getTheme().obtainStyledAttributes(new int[] { android.R.attr.colorBackground });
				mapCover.setBackgroundResource(attributes.getResourceId(0, android.R.color.white));
			}

			mapContainer.addView(mapView);
		} else {

			showUpdateGooglePlayServices();
		}
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		fragmentHelper.onViewCreated(view, savedInstanceState);

		mapLegend = findView(R.id.mapLegend);
	}

	@Override
	public Integer getBaseFragmentLayout() {
		return fragmentHelper.getBaseFragmentLayout();
	}

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.map_fragment;
	}

	/**
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		fragmentHelper.onStart();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		
		fragmentHelper.onResume();
		setUpMap();
	}
	
	protected abstract InfoWindowAdapter getInfoWindowAdapter();
	
	protected abstract void onMapSetup();
	
	protected abstract void onMapSetupFinished();
	
	private void setUpMap() {

		if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(getActivity())) {
			if (permissionGranted) {

				mapLegend.removeAllViews();
				mapLegend.setVisibility(View.GONE);

				// Do a null check to confirm that we have not already instantiated the map.
				if (map == null) {
					// Try to obtain the map from the SupportMapFragment.
					map = getMap();
					// Check if we were successful in obtaining the map.
					if (map != null) {
						map.setMyLocationEnabled(isLocationEnabled() && LocationHelper.get().isLocalizationEnabled());
						map.setOnMyLocationChangeListener(this);
						// Setting an info window adapter allows us to change the both the contents and look of the
						// info window.
						InfoWindowAdapter infoWindowAdapter = getInfoWindowAdapter();
						if (infoWindowAdapter != null) {
							map.setInfoWindowAdapter(infoWindowAdapter);
						}
						map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
						onMapSetup();

						// Pan to see all markers in view. Cannot zoom to bounds until the map has a size.
						final View mapView = getView();
						if (mapView.getViewTreeObserver().isAlive()) {
							mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

								@SuppressWarnings("deprecation")
								@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
								@Override
								public void onGlobalLayout() {

									if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
										mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
									} else {
										mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
									}
									onMapSetupFinished();
									showMap();
								}
							});
						}
					}
				} else {
					map.setMyLocationEnabled(isLocationEnabled() && LocationHelper.get().isLocalizationEnabled());
				}
			} else {
				showPermissionRequiredView();
			}
		} else {
			showUpdateGooglePlayServices();
		}
	}
	
	/**
	 * @see com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener#onMyLocationChange(android.location.Location)
	 */
	@Override
	public void onMyLocationChange(Location location) {
		LocationHelper.get().onMapLocationChanged(location);
	}
	
	private void showMap() {
		mapCover.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if (isResumed()) {
					Animation fadeOut = new FadeOutAnimation(mapCover, DELAY_MILLI);
					mapCover.startAnimation(fadeOut);
				}
			}
		}, DELAY_MILLI);
	}
	
	protected Boolean isLocationEnabled() {
		return false;
	}
	
	public void hideMap() {
		if (isResumed() && (mapCover != null)) {
			mapCover.clearAnimation();
		}
	}
	
	public GoogleMap getGoogleMap() {
		return map;
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAppContext()
	 */
	@Override
	public AppContext getAppContext() {
		return fragmentHelper.getAppContext();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#shouldRetainInstance()
	 */
	@Override
	public Boolean shouldRetainInstance() {
		return fragmentHelper.shouldRetainInstance();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fragmentHelper.onActivityCreated(savedInstanceState);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		fragmentHelper.onBeforePause();
		super.onPause();
		fragmentHelper.onPause();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
		fragmentHelper.onStop();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		fragmentHelper.onDestroyView();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		fragmentHelper.onBeforeDestroy();
		super.onDestroy();
		fragmentHelper.onDestroy();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#findView(int)
	 */
	@Override
	public <V extends View> V findView(int id) {
		return fragmentHelper.findView(id);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#findViewOnActivity(int)
	 */
	@Override
	public <V extends View> V findViewOnActivity(int id) {
		return fragmentHelper.findViewOnActivity(id);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#inflate(int)
	 */
	@Override
	public View inflate(int resource) {
		return fragmentHelper.inflate(resource);
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		fragmentHelper.onStartUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onUpdateUseCase()
	 */
	@Override
	public void onUpdateUseCase() {
		fragmentHelper.onUpdateUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(com.jdroid.java.exception.AbstractException)
	 */
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		fragmentHelper.onFinishFailedUseCase(abstractException);
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		fragmentHelper.onFinishUseCase();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#goBackOnError(com.jdroid.java.exception.AbstractException)
	 */
	@Override
	public Boolean goBackOnError(AbstractException abstractException) {
		return fragmentHelper.goBackOnError(abstractException);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeOnUIThread(java.lang.Runnable)
	 */
	@Override
	public void executeOnUIThread(Runnable runnable) {
		fragmentHelper.executeOnUIThread(runnable);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getInstance(java.lang.Class)
	 */
	@Override
	public <I> I getInstance(Class<I> clazz) {
		return fragmentHelper.<I>getInstance(clazz);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getExtra(java.lang.String)
	 */
	@Override
	public <E> E getExtra(String key) {
		return fragmentHelper.<E>getExtra(key);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getArgument(java.lang.String)
	 */
	@Override
	public <E> E getArgument(String key) {
		return fragmentHelper.<E>getArgument(key);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getArgument(java.lang.String, java.lang.Object)
	 */
	@Override
	public <E> E getArgument(String key, E defaultValue) {
		return fragmentHelper.<E>getArgument(key, defaultValue);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeUseCase(com.jdroid.android.usecase.UseCase)
	 */
	@Override
	public void executeUseCase(UseCase<?> useCase) {
		fragmentHelper.executeUseCase(useCase);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeUseCase(com.jdroid.android.usecase.UseCase, java.lang.Long)
	 */
	@Override
	public void executeUseCase(UseCase<?> useCase, Long delaySeconds) {
		fragmentHelper.executeUseCase(useCase, delaySeconds);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getUser()
	 */
	@Override
	public User getUser() {
		return fragmentHelper.getUser();
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
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return fragmentHelper.getAdSize();
	}

	@Override
	public String getBannerAdUnitId() {
		return fragmentHelper.getBannerAdUnitId();
	}

	@Override
	public HouseAdBuilder getHouseAdBuilder() {
		return fragmentHelper.getHouseAdBuilder();
	}

	/**
	 * @see com.jdroid.android.fragment.FragmentIf#onResumeUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase,
	 *      com.jdroid.android.usecase.listener.DefaultUseCaseListener)
	 */
	@Override
	public void onResumeUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener) {
		fragmentHelper.onResumeUseCase(useCase, listener);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#onResumeUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase,
	 *      com.jdroid.android.usecase.listener.DefaultUseCaseListener,
	 *      com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger)
	 */
	@Override
	public void onResumeUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener,
			UseCaseTrigger useCaseTrigger) {
		fragmentHelper.onResumeUseCase(useCase, listener, useCaseTrigger);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#onPauseUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase,
	 *      com.jdroid.android.usecase.listener.DefaultUseCaseListener)
	 */
	@Override
	public void onPauseUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener) {
		fragmentHelper.onPauseUseCase(useCase, listener);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getActivityIf()
	 */
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
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading()
	 */
	@Override
	public void showLoading() {
		fragmentHelper.showLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissLoading()
	 */
	@Override
	public void dismissLoading() {
		fragmentHelper.dismissLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getDefaultLoading()
	 */
	@Override
	public FragmentLoading getDefaultLoading() {
		return fragmentHelper.getDefaultLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#setLoading(com.jdroid.android.loading.FragmentLoading)
	 */
	@Override
	public void setLoading(FragmentLoading loading) {
		fragmentHelper.setLoading(loading);
	}
	
	/**
	 * @see android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener#onRefresh()
	 */
	@Override
	public void onRefresh() {
		fragmentHelper.onRefresh();
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
}
