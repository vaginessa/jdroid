package com.jdroid.android.ad;

import android.app.Activity;
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
	private AdView adView;
	
	public void loadAd(Activity activity, ViewGroup adViewContainer, AdSize adSize,
			OnClickListener removeAdsClickListener) {
		
		this.adViewContainer = adViewContainer;
		if (adViewContainer != null) {
			AppContext applicationContext = AbstractApplication.get().getAppContext();
			if ((adSize == null) || !applicationContext.areAdsEnabled()) {
				adViewContainer.setVisibility(View.GONE);
			} else {
				
				adView = new AdView(activity);
				adView.setAdUnitId(applicationContext.getAdUnitId());
				adView.setAdSize(adSize);
				
				if (removeAdsClickListener != null) {
					final View removeAdsView = LayoutInflater.from(activity).inflate(R.layout.remove_ads_view,
						adViewContainer, false);
					removeAdsView.setOnClickListener(removeAdsClickListener);
					adViewContainer.addView(removeAdsView);
					
					adView.setAdListener(new AdListener() {
						
						@Override
						public void onAdLoaded() {
							super.onAdLoaded();
							adView.setVisibility(View.VISIBLE);
							removeAdsView.setVisibility(View.GONE);
						}
						
						@Override
						public void onAdClosed() {
							super.onAdClosed();
							adView.setVisibility(View.GONE);
							removeAdsView.setVisibility(View.VISIBLE);
						}
						
						@Override
						public void onAdFailedToLoad(int errorCode) {
							super.onAdFailedToLoad(errorCode);
							adView.setVisibility(View.GONE);
							removeAdsView.setVisibility(View.VISIBLE);
						}
					});
				}
				
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
