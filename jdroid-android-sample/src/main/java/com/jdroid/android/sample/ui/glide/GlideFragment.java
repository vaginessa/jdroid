package com.jdroid.android.sample.ui.glide;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.glide.GlideHelper;
import com.jdroid.android.sample.R;

public class GlideFragment extends AbstractFragment {
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.glide_fragment;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		ImageView imageView = findView(R.id.image);
		
		findView(R.id.withActivity).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				GlideHelper.with(getActivity()).load("http://jdroidtools.com/images/mainImage.png").into(imageView);
			}
		});

		findView(R.id.withFragment).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				GlideHelper.with(GlideFragment.this).load("http://jdroidtools.com/images/android.png").into(imageView);
			}
		});
		
		findView(R.id.withApplicationContext).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				GlideHelper.with(AbstractApplication.get()).load("http://jdroidtools.com/images/gradle.png").into(imageView);
			}
		});
	}
}
