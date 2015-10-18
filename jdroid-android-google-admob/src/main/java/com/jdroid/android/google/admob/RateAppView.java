package com.jdroid.android.google.admob;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.rating.RatingHelper;

import java.util.Random;

public class RateAppView extends RelativeLayout {
	
	public RateAppView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public RateAppView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public RateAppView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(final Context context) {
		
		LayoutInflater.from(context).inflate(R.layout.rate_app_view, this, true);
		
		((TextView)findViewById(R.id.rateAppTitle)).setText(context.getString(R.string.rateMeTitle));
		
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RatingHelper.rateMe(context);
				AbstractApplication.get().getAnalyticsSender().trackRateMeBannerClicked();
			}
			
		});
	}
	
	public static Boolean displayRateMe() {
		Boolean enoughDaysSinceFirstSession = AbstractApplication.get().getAppContext().getDaysSinceFirstSession() > 7;
		return new Random().nextBoolean() && !RatingHelper.isRateMeClicked() && enoughDaysSinceFirstSession;
	}
}
