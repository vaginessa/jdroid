package com.jdroid.android.ad;

import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;

/**
 * 
 * @author Maxi Rosson
 */
public class ZoomAdListener extends AdListener {
	
	private AdView adView;
	
	/**
	 * @param adView
	 */
	public ZoomAdListener(AdView adView) {
		this.adView = adView;
	}
	
	/**
	 * @see com.google.android.gms.ads.AdListener#onAdLoaded()
	 */
	@Override
	public void onAdLoaded() {
		ScaleAnimation zoomIn = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, .5f,
				Animation.RELATIVE_TO_SELF, .5f);
		zoomIn.setDuration(500);
		adView.startAnimation(zoomIn);
	}
}
