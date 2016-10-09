package com.jdroid.android.google.admob.helpers;

import android.app.Activity;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.google.admob.AdMobAppModule;
import com.jdroid.android.google.admob.HouseAdBuilder;
import com.jdroid.android.location.LocationHelper;
import com.jdroid.java.exception.UnexpectedException;

public abstract class BaseAdViewHelper implements AdHelper {

	protected static final String TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";

	private BaseAdViewWrapper baseAdViewWrapper;
	private ViewGroup adViewContainer;
	private AdSize adSize;
	private String defaultAdUnitId;
	private HouseAdBuilder houseAdBuilder;

	private Boolean displayAds = false;

	public BaseAdViewHelper() {
		defaultAdUnitId = AdMobAppModule.get().getAdMobAppContext().getDefaultAdUnitId();
	}

	@Override
	public void loadAd(Activity activity, ViewGroup adViewContainer) {
		if (adViewContainer != null) {
			if ((getAdSize() == null) || !AdMobAppModule.get().getAdMobAppContext().areAdsEnabled()) {
				adViewContainer.setVisibility(View.GONE);
			} else {
				adViewContainer.setVisibility(View.VISIBLE);
				baseAdViewWrapper = createBaseAdViewWrapper(activity);

				if (getDefaultAdUnitId() == null) {
					throw new UnexpectedException("Missing ad unit ID");
				}

				if (!AbstractApplication.get().getAppContext().isProductionEnvironment() && AdMobAppModule.get().getAdMobAppContext().isTestAdUnitIdEnabled()) {
					baseAdViewWrapper.setAdUnitId(BaseAdViewHelper.TEST_AD_UNIT_ID);
				} else {
					baseAdViewWrapper.setAdUnitId(getDefaultAdUnitId());
				}
				baseAdViewWrapper.setAdSize(getAdSize());
				final View customView = getHouseAdBuilder() != null ? getHouseAdBuilder().build(activity) : null;
				if (customView != null) {

					adViewContainer.addView(customView);

					baseAdViewWrapper.setAdListener(new AdListener() {

						@Override
						public void onAdLoaded() {
							if (displayAds) {
								baseAdViewWrapper.getBaseAdView().setVisibility(View.VISIBLE);
								customView.setVisibility(View.GONE);
							} else {
								baseAdViewWrapper.getBaseAdView().postDelayed(new Runnable() {

									@Override
									public void run() {
										displayAds = true;
										if ((baseAdViewWrapper != null) && (customView != null)) {
											baseAdViewWrapper.getBaseAdView().setVisibility(View.VISIBLE);
											customView.setVisibility(View.GONE);
										}
									}
								}, DateUtils.SECOND_IN_MILLIS * 10);
							}
						}

						@Override
						public void onAdClosed() {
							baseAdViewWrapper.getBaseAdView().setVisibility(View.GONE);
							customView.setVisibility(View.VISIBLE);
						}

						@Override
						public void onAdFailedToLoad(int errorCode) {
							baseAdViewWrapper.getBaseAdView().setVisibility(View.GONE);
							customView.setVisibility(View.VISIBLE);
						}
					});
				} else {
					baseAdViewWrapper.setAdListener(new AdListener() {

						@Override
						public void onAdLoaded() {
							baseAdViewWrapper.getBaseAdView().setVisibility(View.VISIBLE);
						}

						@Override
						public void onAdClosed() {
							baseAdViewWrapper.getBaseAdView().setVisibility(View.GONE);
						}

						@Override
						public void onAdFailedToLoad(int errorCode) {
							baseAdViewWrapper.getBaseAdView().setVisibility(View.GONE);
						}
					});
				}

				AppContext applicationContext = AbstractApplication.get().getAppContext();
				AdRequest.Builder builder = createBuilder(applicationContext);
				baseAdViewWrapper.loadAd(builder.build());
				adViewContainer.addView(baseAdViewWrapper.getBaseAdView());
			}
		}
	}

	protected abstract BaseAdViewWrapper createBaseAdViewWrapper(Activity activity);

	protected AdRequest.Builder createBuilder(AppContext applicationContext) {
		final AdRequest.Builder builder = new AdRequest.Builder();
		if (!applicationContext.isProductionEnvironment()) {
			builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
			for (String deviceId : AdMobAppModule.get().getAdMobAppContext().getTestDevicesIds()) {
				builder.addTestDevice(deviceId);
			}
		}
		builder.setLocation(LocationHelper.get().getLocation());
		return builder;
	}

	public AdHelper setAdSize(AdSize adSize) {
		this.adSize = adSize;
		return this;
	}

	public AdSize getAdSize() {
		return adSize;
	}

	public AdHelper setHouseAdBuilder(HouseAdBuilder houseAdBuilder) {
		this.houseAdBuilder = houseAdBuilder;
		return this;
	}

	public HouseAdBuilder getHouseAdBuilder() {
		return houseAdBuilder;
	}

	@Override
	public AdHelper setAdUnitId(String adUnitId) {
		this.defaultAdUnitId = adUnitId;
		return this;
	}

	public String getDefaultAdUnitId() {
		return defaultAdUnitId;
	}

	public void onPause() {
		if (baseAdViewWrapper != null) {
			baseAdViewWrapper.pause();
		}
	}

	public void onResume() {
		if (baseAdViewWrapper != null) {
			if (AdMobAppModule.get().getAdMobAppContext().areAdsEnabled()) {
				if (adViewContainer != null) {
					adViewContainer.setVisibility(View.VISIBLE);
				}
				baseAdViewWrapper.resume();
			} else if (adViewContainer != null) {
				adViewContainer.removeView(baseAdViewWrapper.getBaseAdView());
				adViewContainer.setVisibility(View.GONE);
				baseAdViewWrapper = null;
				adViewContainer = null;
			}
		}

	}

	public void onDestroy() {
		if (baseAdViewWrapper != null) {
			baseAdViewWrapper.destroy();
			baseAdViewWrapper = null;
			adViewContainer = null;
		}
	}
}
