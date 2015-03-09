package com.jdroid.android.debug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.ScreenUtils;
import com.jdroid.java.utils.StringUtils;

public class DebugInfoView extends LinearLayout {
	
	public DebugInfoView(Context context) {
		super(context);
		
		LayoutInflater.from(context).inflate(R.layout.debug_info_view, this, true);
		
		TextView packageName = (TextView)findViewById(R.id.packageName);
		packageName.setText(context.getString(R.string.packageName, AndroidUtils.getPackageName()));
		
		final AppContext applicationContext = AbstractApplication.get().getAppContext();
		TextView buildType = (TextView)findViewById(R.id.buildType);
		buildType.setText(context.getString(R.string.buildType,
				AbstractApplication.get().getAppContext().getBuildType()));
		
		TextView analyticsEnabled = (TextView)findViewById(R.id.analyticsEnabled);
		analyticsEnabled.setText(context.getString(R.string.analyticsEnabled,
			applicationContext.isGoogleAnalyticsEnabled()));

		TextView adsEnabled = (TextView)findViewById(R.id.adsEnabled);
		adsEnabled.setText(context.getString(R.string.adsEnabled,
			applicationContext.areAdsEnabled()));

		TextView smallestScreenWidthDp = (TextView)findViewById(R.id.smallestScreenWidthDp);
		smallestScreenWidthDp.setText(context.getString(R.string.smallestScreenWidthDp,
			ScreenUtils.getSmallestScreenWidthDp()));
		
		TextView screenDensity = (TextView)findViewById(R.id.screenDensity);
		screenDensity.setText(context.getString(R.string.screenDensity, ScreenUtils.getScreenDensity()));
		
		TextView gitBranch = (TextView)findViewById(R.id.gitBranch);
		if (AbstractApplication.get().getGitContext().getBranch() != null) {
			gitBranch.setText(context.getString(R.string.gitBranch, AbstractApplication.get().getGitContext().getBranch()));
		} else {
			gitBranch.setVisibility(View.GONE);
		}
		
		TextView gitSha = (TextView)findViewById(R.id.gitSha);
		if (AbstractApplication.get().getGitContext().getSha() != null) {
			gitSha.setText(context.getString(R.string.gitSha, AbstractApplication.get().getGitContext().getSha()));
		} else {
			gitSha.setVisibility(View.GONE);
		}
		
		TextView networkOperatorName = (TextView)findViewById(R.id.networkOperatorName);
		if (StringUtils.isNotBlank(AndroidUtils.getNetworkOperatorName())) {
			networkOperatorName.setText(context.getString(R.string.networkOperatorName,
				AndroidUtils.getNetworkOperatorName()));
		} else {
			networkOperatorName.setVisibility(View.GONE);
		}
		
		TextView simOperatorName = (TextView)findViewById(R.id.simOperatorName);
		if (StringUtils.isNotBlank(AndroidUtils.getSimOperatorName())) {
			simOperatorName.setText(context.getString(R.string.simOperatorName, AndroidUtils.getSimOperatorName()));
		} else {
			simOperatorName.setVisibility(View.GONE);
		}
	}
}
