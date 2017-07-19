package com.jdroid.android.firebase.admob.helpers;

import android.app.Activity;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.firebase.admob.AdMobAppModule;
import com.jdroid.android.location.LocationHelper;
import com.jdroid.java.exception.UnexpectedException;

public class InterstitialAdHelper implements AdHelper {

	private static final String TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";

	private InterstitialAd interstitial;
	private Boolean displayInterstitial = false;
	private String interstitialAdUnitId;

	public InterstitialAdHelper() {
		interstitialAdUnitId = AdMobAppModule.get().getAdMobAppContext().getDefaultAdUnitId();
	}

	private AdRequest.Builder createBuilder(AppContext applicationContext) {
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

	@Override
	public void loadAd(Activity activity, ViewGroup adViewContainer) {
		if (AdMobAppModule.get().getAdMobAppContext().areAdsEnabled()) {
			interstitial = new InterstitialAd(activity);

			if (interstitialAdUnitId == null) {
				throw new UnexpectedException("Missing interstitial ad unit ID");
			}

			if (!AbstractApplication.get().getAppContext().isProductionEnvironment() && AdMobAppModule.get().getAdMobAppContext().isTestAdUnitIdEnabled()) {
				interstitial.setAdUnitId(TEST_AD_UNIT_ID);
			} else {
				interstitial.setAdUnitId(interstitialAdUnitId);
			}

			AppContext applicationContext = AbstractApplication.get().getAppContext();
			AdRequest.Builder builder = createBuilder(applicationContext);
			interstitial.loadAd(builder.build());
			interstitial.setAdListener(new AdListener() {

				@Override
				public void onAdLoaded() {
					super.onAdLoaded();
					if (displayInterstitial) {
						displayInterstitial(false);
					}
				}
			});
		}
	}

	public void displayInterstitial(Boolean retryIfNotLoaded) {
		displayInterstitial = retryIfNotLoaded;
		if ((interstitial != null) && interstitial.isLoaded()) {
			interstitial.show();
			displayInterstitial = false;
		}
	}

	@Override
	public AdHelper setAdUnitId(String adUnitId) {
		this.interstitialAdUnitId = adUnitId;
		return this;
	}
}
