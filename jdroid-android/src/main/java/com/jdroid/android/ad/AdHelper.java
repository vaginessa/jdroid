package com.jdroid.android.ad;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.context.DefaultApplicationContext;

/**
 * 
 * @author Maxi Rosson
 */
public class AdHelper {
	
	private ViewGroup adViewContainer;
	private AdView adView;
	
	public void loadAd(Activity activity, ViewGroup adViewContainer, AdSize adSize) {
		
		this.adViewContainer = adViewContainer;
		if (adViewContainer != null) {
			DefaultApplicationContext applicationContext = AbstractApplication.get().getAndroidApplicationContext();
			if ((adSize == null) || !applicationContext.areAdsEnabled()) {
				adViewContainer.setVisibility(View.GONE);
			} else {
				
				adView = new AdView(activity);
				adView.setAdUnitId(applicationContext.getAdUnitId());
				adView.setAdSize(adSize);
				
				AdRequest.Builder builder = new AdRequest.Builder();
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
			if (AbstractApplication.get().getAndroidApplicationContext().areAdsEnabled()) {
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
