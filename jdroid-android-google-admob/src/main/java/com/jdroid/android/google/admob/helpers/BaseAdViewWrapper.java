package com.jdroid.android.google.admob.helpers;

import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;

public class BaseAdViewWrapper {

	private View baseAdView;
	private Boolean isAdView;

	public BaseAdViewWrapper(View baseAdView) {
		this.baseAdView = baseAdView;
		this.isAdView = baseAdView instanceof AdView;
	}

	public void setAdUnitId(String adUnitId) {
		if (isAdView) {
			((AdView)baseAdView).setAdUnitId(adUnitId);
		} else {
			((NativeExpressAdView)baseAdView).setAdUnitId(adUnitId);
		}
	}

	public void setAdSize(AdSize adSize) {
		if (isAdView) {
			((AdView)baseAdView).setAdSize(adSize);
		} else {
			((NativeExpressAdView)baseAdView).setAdSize(adSize);
		}
	}

	public void setAdListener(AdListener adListener) {
		if (isAdView) {
			((AdView)baseAdView).setAdListener(adListener);
		} else {
			((NativeExpressAdView)baseAdView).setAdListener(adListener);
		}
	}

	public void loadAd(AdRequest adRequest) {
		if (isAdView) {
			((AdView)baseAdView).loadAd(adRequest);
		} else {
			((NativeExpressAdView)baseAdView).loadAd(adRequest);
		}
	}

	public void pause() {
		if (isAdView) {
			((AdView)baseAdView).pause();
		} else {
			((NativeExpressAdView)baseAdView).pause();
		}
	}

	public void resume() {
		if (isAdView) {
			((AdView)baseAdView).resume();
		} else {
			((NativeExpressAdView)baseAdView).resume();
		}
	}

	public void destroy() {
		if (isAdView) {
			((AdView)baseAdView).destroy();
		} else {
			((NativeExpressAdView)baseAdView).destroy();
		}
	}

	public View getBaseAdView() {
		return baseAdView;
	}
}
