package com.jdroid.android.navdrawer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jdroid.android.R;
import com.jdroid.android.activity.ComponentIf;
import com.jdroid.android.utils.ImageLoaderUtils;

public class NavDrawerHeaderBuilder {

	private View navDrawerHeader;

	public NavDrawerHeaderBuilder(ComponentIf componentIf) {
		navDrawerHeader = componentIf.inflate(R.layout.nav_drawer_header);
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
		ImageLoaderUtils.displayImage(imageUrl,
				((ImageView)navDrawerHeader.findViewById(R.id.cover)), null, null, ttl);
	}

	public void setMainImage(Integer imageResId) {
		((ImageView)navDrawerHeader.findViewById(R.id.photo)).setImageResource(imageResId);
	}

	public void setMainImage(String imageUrl, Long ttl) {
		ImageLoaderUtils.displayImage(imageUrl,
				((ImageView)navDrawerHeader.findViewById(R.id.photo)), R.drawable.profile_default, null,
				ttl);
	}

	public View build() {
		return navDrawerHeader;
	}
}
