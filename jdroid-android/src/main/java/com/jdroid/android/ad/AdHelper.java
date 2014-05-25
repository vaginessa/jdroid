package com.jdroid.android.ad;

import android.app.Activity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.context.AppContext;

/**
 * 
 * @author Maxi Rosson
 */
public class AdHelper {
	
	private ViewGroup adViewContainer;
	private View customView;
	private AdView adView;
	private Boolean displayAds = false;
	
	public void loadAd(final Activity activity, ViewGroup adViewContainer, AdSize adSize,
			final OnClickListener removeAdsClickListener) {
		
		this.adViewContainer = adViewContainer;
		if (adViewContainer != null) {
			AppContext applicationContext = AbstractApplication.get().getAppContext();
			if ((adSize == null) || !applicationContext.areAdsEnabled()) {
				adViewContainer.setVisibility(View.GONE);
			} else {
				
				adView = new AdView(activity);
				adView.setAdUnitId(applicationContext.getAdUnitId());
				adView.setAdSize(adSize);
				
				if (RateAppView.displayRateMe()) {
					customView = new RateAppView(activity);
				} else if (removeAdsClickListener != null) {
					customView = LayoutInflater.from(activity).inflate(R.layout.remove_ads_view, adViewContainer, false);
					customView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							removeAdsClickListener.onClick(v);
							AbstractApplication.get().getAnalyticsSender().trackRemoveAdsBannerClicked();
						}
					});
				}
				
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
								}, DateUtils.SECOND_IN_MILLIS * 5);
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
				
				final AdRequest.Builder builder = new AdRequest.Builder();
				if (!applicationContext.isProductionEnvironment()) {
					builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
					for (String deviceId : applicationContext.getTestDevicesIds()) {
						builder.addTestDevice(deviceId);
					}
				}
				
				adView.loadAd(builder.build());
				adViewContainer.addView(adView);
			}
		}
	}
	
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
	}
	
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
	
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
			adView = null;
			adViewContainer = null;
		}
	}
}
