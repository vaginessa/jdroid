package com.jdroid.android.google.maps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.ad.AdHelper;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.exception.SnackbarErrorDisplayer;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.fragment.FragmentIf;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.permission.PermissionHelper;
import com.jdroid.android.snackbar.SnackbarBuilder;
import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.android.usecase.UseCase;
import com.jdroid.android.usecase.listener.DefaultUseCaseListener;
import com.jdroid.java.exception.AbstractException;

public abstract class AbstractMapFragment extends SupportMapFragment implements FragmentIf {
	
	private FragmentHelper fragmentHelper;
	private GoogleMap map;

	private ViewGroup mapContainer;
	private View updateGoogleplayServicesContainer;

	private Boolean locationPermissionGranted = false;
	private PermissionHelper locationPermissionHelper;

	protected FragmentIf getFragmentIf() {
		return (FragmentIf)this.getActivity();
	}

	/**
	 Calling this before super.oncreate() when you try to use a toolbar and the view contains a map
	 https://code.google.com/p/android/issues/detail?id=175140
	 */
	public static void doMapToolbarWorkaround(Bundle savedInstanceState){
		// FIXME This is just a workaround to the following error: ClassNotFoundException when unmarshalling android.support.v7.widget.Toolbar$SavedState
		// It seems to be a problem with the SupportMapFragment implementation
		// https://code.google.com/p/android/issues/detail?id=175140
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
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		doMapToolbarWorkaround(savedInstanceState);
		super.onCreate(savedInstanceState);
		fragmentHelper = AbstractApplication.get().createFragmentHelper(this);
		fragmentHelper.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			setRetainInstance(true);
		}

		if (isLocationEnabled()) {
			locationPermissionHelper = PermissionHelper.createLocationPermissionHelper(this);
			locationPermissionHelper.setAppInfoDialogMessageResId(R.string.locationPermissionRequired);
			locationPermissionHelper.setOnRequestPermissionsResultListener(new PermissionHelper.OnRequestPermissionsResultListener() {
				@Override
				public void onRequestPermissionsGranted() {
					locationPermissionGranted = true;
					if (map != null) {
						map.setMyLocationEnabled(true);
					}
				}

				@Override
				public void onRequestPermissionsDenied() {
					// Nothing to do
				}
			});
		}

		getMapAsync(new OnMapReadyCallback() {

			@Override
			public void onMapReady(final GoogleMap googleMap) {
				map = googleMap;

				// Setting an info window adapter allows us to change the both the contents and look of the
				// info window.
				InfoWindowAdapter infoWindowAdapter = getInfoWindowAdapter();
				if (infoWindowAdapter != null) {
					map.setInfoWindowAdapter(infoWindowAdapter);
				}
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				AbstractMapFragment.this.onMapReady(map);

				getGoogleMap().setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
					@Override
					public void onMapLoaded() {
						AbstractMapFragment.this.onMapLoaded(googleMap);
					}
				});

				if (locationPermissionHelper != null) {
					locationPermissionGranted = locationPermissionHelper.checkPermission(false);
					if (locationPermissionGranted) {
						map.setMyLocationEnabled(true);
					} else if (snackbarToSuggestLocationPermissionEnabled()) {
						locationPermissionHelper.setOnRequestPermissionsResultListener(new PermissionHelper.OnRequestPermissionsResultListener() {
							@Override
							public void onRequestPermissionsGranted() {
								// Do nothing
							}

							@Override
							public void onRequestPermissionsDenied() {
								SnackbarBuilder snackbarBuilder = new SnackbarBuilder();
								snackbarBuilder.setActionTextResId(R.string.allow);
								snackbarBuilder.setDuration(5000);
								snackbarBuilder.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										locationPermissionHelper.setOnRequestPermissionsResultListener(null);
										locationPermissionGranted = locationPermissionHelper.checkPermission(true);
									}
								});
								snackbarBuilder.setDescription(R.string.locationPermissionSuggested);
								snackbarBuilder.build(getActivity()).show();
							}
						});
					}
				}
			}
		});
	}

	protected Boolean snackbarToSuggestLocationPermissionEnabled() {
		return false;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = fragmentHelper.onCreateView(inflater, container, savedInstanceState);
		mapContainer = (ViewGroup)view.findViewById(R.id.mapContainer);
		View mapView = super.onCreateView(inflater, mapContainer, savedInstanceState);
		mapContainer.addView(mapView);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		fragmentHelper.onViewCreated(view, savedInstanceState);

		updateGoogleplayServicesContainer = findView(R.id.updateGoogleplayServicesContainer);
	}

	protected void onMapReady(@NonNull GoogleMap googleMap) {
		// Do Nothing
	}

	protected void onMapLoaded(@NonNull GoogleMap googleMap) {
		// Do Nothing
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

		if (locationPermissionHelper != null) {
			locationPermissionHelper.onResume();
		}

		if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(getActivity())) {
			displayMap();
		} else {
			showUpdateGooglePlayServices();
		}
	}

	private void displayMap() {
		updateGoogleplayServicesContainer.setVisibility(View.GONE);
		mapContainer.setVisibility(View.VISIBLE);
	}

	private void showUpdateGooglePlayServices() {
		mapContainer.setVisibility(View.GONE);
		updateGoogleplayServicesContainer.setVisibility(View.VISIBLE);

		Button button = findView(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				GooglePlayServicesUtils.launchGooglePlayServicesUpdate(getActivity());
			}
		});
	}
	
	protected InfoWindowAdapter getInfoWindowAdapter() {
		return null;
	}
	
	protected Boolean isLocationEnabled() {
		return false;
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

	@Nullable
	@Override
	public AdHelper createAdHelper() {
		return fragmentHelper.createAdHelper();
	}

	@Nullable
	@Override
	public AdHelper getAdHelper() {
		return fragmentHelper.getAdHelper();
	}
}
