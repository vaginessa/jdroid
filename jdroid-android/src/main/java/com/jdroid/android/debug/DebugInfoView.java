package com.jdroid.android.debug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.context.DefaultApplicationContext;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.context.GitContext;
import com.jdroid.java.utils.StringUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class DebugInfoView extends LinearLayout {
	
	public DebugInfoView(Context context) {
		super(context);
		
		LayoutInflater.from(context).inflate(R.layout.debug_info_view, this, true);
		
		TextView packageName = (TextView)findViewById(R.id.packageName);
		packageName.setText(context.getString(R.string.packageName, AndroidUtils.getPackageName()));
		
		final DefaultApplicationContext applicationContext = AbstractApplication.get().getAndroidApplicationContext();
		TextView environmentName = (TextView)findViewById(R.id.environmentName);
		environmentName.setText(context.getString(R.string.environmentName,
			applicationContext.getEnvironment().toString()));
		
		TextView analyticsEnabled = (TextView)findViewById(R.id.analyticsEnabled);
		analyticsEnabled.setText(context.getString(R.string.analyticsEnabled,
			applicationContext.isGoogleAnalyticsEnabled()));
		
		TextView smallestScreenWidthDp = (TextView)findViewById(R.id.smallestScreenWidthDp);
		smallestScreenWidthDp.setText(context.getString(R.string.smallestScreenWidthDp,
			AndroidUtils.getSmallestScreenWidthDp()));
		
		TextView screenDensity = (TextView)findViewById(R.id.screenDensity);
		screenDensity.setText(context.getString(R.string.screenDensity, AndroidUtils.getScreenDensity()));
		
		TextView branch = (TextView)findViewById(R.id.branch);
		if (GitContext.get().getBranch() != null) {
			branch.setText(context.getString(R.string.branch, GitContext.get().getBranch()));
		} else {
			branch.setVisibility(View.GONE);
		}
		
		TextView commitId = (TextView)findViewById(R.id.commitId);
		if (GitContext.get().getCommitId() != null) {
			commitId.setText(context.getString(R.string.commitId, GitContext.get().getCommitId()));
		} else {
			commitId.setVisibility(View.GONE);
		}
		
		TextView commitTime = (TextView)findViewById(R.id.commitTime);
		if (GitContext.get().getCommitTime() != null) {
			commitTime.setText(context.getString(R.string.commitTime, GitContext.get().getCommitTime()));
		} else {
			commitTime.setVisibility(View.GONE);
		}
		
		TextView buildTime = (TextView)findViewById(R.id.buildTime);
		if (GitContext.get().getBuildTime() != null) {
			buildTime.setText(context.getString(R.string.buildTime, GitContext.get().getBuildTime()));
		} else {
			buildTime.setVisibility(View.GONE);
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
		
		findViewById(R.id.crash).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String crashType = applicationContext.getCrashType();
				Boolean workerThread = crashType.endsWith("Worker Thread");
				CrashGenerator.crash(crashType, workerThread);
			}
		});
	}
}
