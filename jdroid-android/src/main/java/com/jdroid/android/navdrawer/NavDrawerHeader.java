package com.jdroid.android.navdrawer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.activity.ComponentIf;

public class NavDrawerHeader {

	private View navDrawerHeader;

	public NavDrawerHeader(ComponentIf componentIf) {
		navDrawerHeader = componentIf.findView(R.id.navDrawerHeader);
	}

	public void setTitle(String title) {
		((TextView)navDrawerHeader.findViewById(R.id.title)).setText(title);
	}

	public void setSubTitle(String subTitle) {
		((TextView)navDrawerHeader.findViewById(R.id.subTitle)).setText(subTitle);
	}

	public void setBackground(Integer imageResId) {
		((ImageView)navDrawerHeader.findViewById(R.id.cover)).setImageResource(imageResId);
	}

	public void setBackground(String imageUrl, Long ttl) {
		AbstractApplication.get().getImageLoaderHelper().displayImage(imageUrl,
				((ImageView)navDrawerHeader.findViewById(R.id.cover)), null, null, ttl);
	}

	public void setMainImage(Integer imageResId) {
		((ImageView)navDrawerHeader.findViewById(R.id.photo)).setImageResource(imageResId);
	}

	public void setMainImage(String imageUrl, Long ttl) {
		AbstractApplication.get().getImageLoaderHelper().displayImage(imageUrl,
				((ImageView)navDrawerHeader.findViewById(R.id.photo)), R.drawable.profile_default, null,
				ttl);
	}
}
