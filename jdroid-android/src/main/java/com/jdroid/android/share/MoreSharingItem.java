package com.jdroid.android.share;

import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.R;

public abstract class MoreSharingItem extends SharingItem {
	
	/**
	 * @see com.jdroid.android.share.SharingItem#getAppIcon()
	 */
	@Override
	public Drawable getAppIcon() {
		Drawable drawable = DrawableCompat.wrap(AbstractApplication.get().getResources().getDrawable(R.drawable.more_selector));
		DrawableCompat.setTint(drawable, AbstractApplication.get().getResources().getColor(R.color.colorPrimary));
		return drawable;
	}
	
	/**
	 * @see com.jdroid.android.share.SharingItem#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return true;
	}
	
	/**
	 * @see com.jdroid.android.share.SharingItem#getPackageName()
	 */
	@Override
	public String getPackageName() {
		return null;
	}
}
