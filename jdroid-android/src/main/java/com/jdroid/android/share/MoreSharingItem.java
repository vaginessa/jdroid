package com.jdroid.android.share;

import android.graphics.drawable.Drawable;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;

public abstract class MoreSharingItem extends SharingItem {
	
	/**
	 * @see com.jdroid.android.share.SharingItem#getAppIcon()
	 */
	@Override
	public Drawable getAppIcon() {
		return AbstractApplication.get().getResources().getDrawable(R.drawable.more_selector);
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
