package com.jdroid.android.google.admob;

import android.app.Activity;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.jdroid.android.ad.AdHelper;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.location.LocationHelper;
import com.jdroid.java.exception.UnexpectedException;

public class AdMobAdHelper implements AdHelper {

	private View customView;
	private Boolean displayAds = false;

	private ViewGroup adViewContainer;
	private AdView adView;
	private AdSize adSize;
	private String bannerAdUnitId;

	private InterstitialAd interstitial;
	private Boolean displayInterstitial = false;
	private String interstitialAdUnitId;
	private Boolean isInterstitialEnabled = false;

	private HouseAdBuilder houseAdBuilder;

	public AdMobAdHelper() {
		bannerAdUnitId = AbstractApplication.get().getAppContext().getDefaultAdUnitId();
		interstitialAdUnitId = AbstractApplication.get().getAppContext().getDefaultAdUnitId();
	}

	@Override
	public void loadBanner(final Activity activity) {

		AppContext applicationContext = AbstractApplication.get().getAppContext();

		if (adViewContainer != null) {
			if ((adSize == null) || !applicationContext.areAdsEnabled()) {
				adViewContainer.setVisibility(View.GONE);
			} else {

				adView = new AdView(activity);

				if (bannerAdUnitId == null) {
					throw new UnexpectedException("Missing banner ad unit ID");
				}

				adView.setAdUnitId(bannerAdUnitId);
				adView.setAdSize(adSize);
				customView = houseAdBuilder != null ? houseAdBuilder.build(activity) : null;
				if (customView != null) {

					adViewContainer.addView(customView);

					adView.setAdListener(new AdListener() {

						@Override
						public void onAdLoaded() {
							super.onAdLoaded();

							if (displayAds) {
								adView.setVisibility(View.VISIBLE);
								customView.setVisibility(View.GONE);
							} else {
								adView.postDelayed(new Runnable() {

									@Override
									public void run() {
										displayAds = true;
										if ((adView != null) && (customView != null)) {
											adView.setVisibility(View.VISIBLE);
											customView.setVisibility(View.GONE);
										}
									}
								}, DateUtils.SECOND_IN_MILLIS * 10);
							}
						}

						@Override
						public void onAdClosed() {
							super.onAdClosed();
							adView.setVisibility(View.GONE);
							customView.setVisibility(View.VISIBLE);
						}

						@Override
						public void onAdFailedToLoad(int errorCode) {
							super.onAdFailedToLoad(errorCode);
							adView.setVisibility(View.GONE);
							customView.setVisibility(View.VISIBLE);
						}
					});
				}

				AdRequest.Builder builder = createBuilder(applicationContext);
				adView.loadAd(builder.build());
				adViewContainer.addView(adView);
			}
		}
	}

	private AdRequest.Builder createBuilder(AppContext applicationContext) {
		final AdRequest.Builder builder = new AdRequest.Builder();
		if (!applicationContext.isProductionEnvironment()) {
			builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
			for (String deviceId : applicationContext.getTestDevicesIds()) {
				builder.addTestDevice(deviceId);
			}
		}
		builder.setLocation(LocationHelper.get().getLocation());
		return builder;
	}

	@Override
	public void loadInterstitial(Activity activity) {
		AppContext applicationContext = AbstractApplication.get().getAppContext();
		if (isInterstitialEnabled && applicationContext.areAdsEnabled()) {
			interstitial = new InterstitialAd(activity);

			if (interstitialAdUnitId == null) {
				throw new UnexpectedException("Missing interstitial ad unit ID");
			}

			interstitial.setAdUnitId(interstitialAdUnitId);

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

				@Override
				public void onAdOpened() {
					super.onAdOpened();
					AbstractApplication.get().getAnalyticsSender().onActivityStart(AdActivity.class, null, null);
				}

			});
		}
	}

	@Override
	public void displayInterstitial(Boolean retryIfNotLoaded) {
		displayInterstitial = retryIfNotLoaded;
		if ((interstitial != null) && interstitial.isLoaded()) {
			interstitial.show();
			displayInterstitial = false;
		}
	}

	@Override
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
	}

	@Override
	public void onResume() {
		if (adView != null) {
			if (AbstractApplication.get().getAppContext().areAdsEnabled()) {
				adView.resume();
			} else if (adViewContainer != null) {
				adViewContainer.removeView(adView);
				adViewContainer.setVisibility(View.GONE);
				adView = null;
				adViewContainer = null;
			}
		}

	}

	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
			adView = null;
			adViewContainer = null;
		}
	}

	public AdHelper setAdSize(AdSize adSize) {
		this.adSize = adSize;
		return this;
	}

	public AdHelper setHouseAdBuilder(HouseAdBuilder houseAdBuilder) {
		this.houseAdBuilder = houseAdBuilder;
		return this;
	}

	@Override
	public AdHelper setAdViewContainer(ViewGroup adViewContainer) {
		this.adViewContainer = adViewContainer;
		return this;
	}

	@Override
	public AdHelper setBannerAdUnitId(String bannerAdUnitId) {
		this.bannerAdUnitId = bannerAdUnitId;
		return this;
	}

	@Override
	public AdHelper setInterstitialAdUnitId(String interstitialAdUnitId) {
		this.interstitialAdUnitId = interstitialAdUnitId;
		return this;
	}

	@Override
	public AdHelper setIsInterstitialEnabled(Boolean isInterstitialEnabled) {
		this.isInterstitialEnabled = isInterstitialEnabled;
		return this;
	}
}
