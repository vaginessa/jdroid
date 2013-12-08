package com.jdroid.android.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.jdroid.android.R;
import com.jdroid.android.fragment.FragmentIf;

public class LoadingLayout extends FrameLayout {
	
	private ProgressBar progressLoading;
	
	public LoadingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public LoadingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public LoadingLayout(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.non_blocking_loading, this, true);
		progressLoading = (ProgressBar)this.findViewById(R.id.loadingProgressBar);
		
	}
	
	public void showLoading(FragmentIf fragmentIf) {
		if (fragmentIf != null) {
			fragmentIf.executeOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					for (int i = 0; i < getChildCount(); i++) {
						View child = getChildAt(i);
						if (child == progressLoading) {
							animateVisibility(child, VISIBLE);
						} else {
							animateVisibility(child, GONE);
						}
					}
				}
			});
		}
	}
	
	public void dismissLoading(FragmentIf fragmentIf) {
		if (fragmentIf != null) {
			fragmentIf.executeOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					for (int i = 0; i < getChildCount(); i++) {
						View child = getChildAt(i);
						if (child == progressLoading) {
							animateVisibility(child, GONE);
						} else {
							animateVisibility(child, VISIBLE);
						}
					}
				}
			});
		}
		
	}
	
	public boolean isLoadingVisible() {
		return progressLoading.getVisibility() == View.VISIBLE;
	}
	
	public void animateVisibility(final View view, final int visibility) {
		if ((visibility == VISIBLE) && (view.getVisibility() != VISIBLE)) {
			view.clearAnimation();
			view.setVisibility(VISIBLE);
			Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
			animation.setFillBefore(true);
			view.startAnimation(animation);
		} else if ((visibility != VISIBLE) && (view.getVisibility() == VISIBLE)) {
			view.clearAnimation();
			view.setVisibility(visibility);
			Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
			animation.setFillBefore(true);
			view.startAnimation(animation);
		}
	}
	
}
