package com.jdroid.android.sample.application;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.jdroid.android.glide.AbstractJdroidAppGlideModule;

@GlideModule
public class SampleAppGlideModule extends AbstractJdroidAppGlideModule {
	
	@Override
	public void applyOptions(Context context, GlideBuilder builder) {
		super.applyOptions(context, builder);
		
	}
}
