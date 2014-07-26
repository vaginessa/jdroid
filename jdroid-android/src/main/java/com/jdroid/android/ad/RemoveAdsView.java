package com.jdroid.android.ad;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;

/**
 * 
 * @author Maxi Rosson
 */
public class RemoveAdsView extends RelativeLayout {
	
	public RemoveAdsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public RemoveAdsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public RemoveAdsView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(final Context context) {
		LayoutInflater.from(context).inflate(R.layout.remove_ads_view, this, true);
	}
	
	/**
	 * @see android.view.View#setOnClickListener(android.view.View.OnClickListener)
	 */
	@Override
	public void setOnClickListener(final OnClickListener removeAdsClickListener) {
		super.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				removeAdsClickListener.onClick(v);
				AbstractApplication.get().getAnalyticsSender().trackRemoveAdsBannerClicked();
			}
		});
	}
	
	public static Boolean displayRemoveAdsView() {
		return AbstractApplication.get().getAppContext().getDaysSinceFirstSession() > 3;
	}
}
