package com.jdroid.android.share;

import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.R;

public abstract class MoreSharingItem extends SharingItem {
	
	@Override
	public Drawable getAppIcon() {
		Drawable drawable = DrawableCompat.wrap(AbstractApplication.get().getResources().getDrawable(R.drawable.jdroid_more_selector));
		DrawableCompat.setTint(drawable, AbstractApplication.get().getResources().getColor(R.color.jdroid_colorPrimary));
		return drawable;
	}
	
	@Override
	public Boolean isEnabled() {
		return true;
	}
	
	@Override
	public String getPackageName() {
		return null;
	}
}
