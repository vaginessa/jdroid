package com.jdroid.android.debug;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.context.DefaultApplicationContext;
import com.jdroid.android.exception.CommonErrorCode;
import com.jdroid.android.fragment.AbstractPreferenceFragment;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.context.GitContext;
import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.utils.ExecutorUtils;

/**
 * 
 * @author Maxi Rosson
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DebugSettingsFragment extends AbstractPreferenceFragment {
	
	private static final String CRASH_MESSAGE = "This is a generated crash for testing";
	
	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		addPreferencesFromResource(R.xml.debug_preferences);
	}
	
	/**
	 * @see android.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		View debugInfoView = inflateDebugInfoView(getActivity());
		
		ListView listView = ((ListView)findView(android.R.id.list));
		listView.addFooterView(debugInfoView);
	}
	
	public static View inflateDebugInfoView(Activity activity) {
		View debugInfoView = LayoutInflater.from(activity).inflate(R.layout.debug_info_view, null);
		
		TextView packageName = (TextView)debugInfoView.findViewById(R.id.packageName);
		packageName.setText(activity.getString(R.string.packageName, AndroidUtils.getPackageName()));
		
		final DefaultApplicationContext applicationContext = AbstractApplication.get().getAndroidApplicationContext();
		TextView environmentName = (TextView)debugInfoView.findViewById(R.id.environmentName);
		environmentName.setText(activity.getString(R.string.environmentName, applicationContext.getEnvironmentName()));
		
		TextView analyticsEnabled = (TextView)debugInfoView.findViewById(R.id.analyticsEnabled);
		analyticsEnabled.setText(activity.getString(R.string.analyticsEnabled, applicationContext.isAnalyticsEnabled()));
		
		TextView screenSize = (TextView)debugInfoView.findViewById(R.id.screenSize);
		screenSize.setText(activity.getString(R.string.screenSize, AndroidUtils.getScreenSize()));
		
		TextView screenDensity = (TextView)debugInfoView.findViewById(R.id.screenDensity);
		screenDensity.setText(activity.getString(R.string.screenDensity, AndroidUtils.getScreenDensity()));
		
		TextView commitId = (TextView)debugInfoView.findViewById(R.id.commitId);
		if (GitContext.get().getCommitId() != null) {
			commitId.setText(activity.getString(R.string.commitId, GitContext.get().getCommitId()));
		} else {
			commitId.setVisibility(View.GONE);
		}
		
		TextView commitTime = (TextView)debugInfoView.findViewById(R.id.commitTime);
		if (GitContext.get().getCommitTime() != null) {
			commitTime.setText(activity.getString(R.string.commitTime, GitContext.get().getCommitTime()));
		} else {
			commitTime.setVisibility(View.GONE);
		}
		
		TextView buildTime = (TextView)debugInfoView.findViewById(R.id.buildTime);
		if (GitContext.get().getBuildTime() != null) {
			buildTime.setText(activity.getString(R.string.buildTime, GitContext.get().getBuildTime()));
		} else {
			buildTime.setVisibility(View.GONE);
		}
		
		debugInfoView.findViewById(R.id.crash).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String crashType = applicationContext.getCrashType();
				Boolean workerThread = crashType.endsWith("Worker Thread");
				Runnable runnable = new Runnable() {
					
					@Override
					public void run() {
						if (crashType.startsWith("BusinessException")) {
							throw CommonErrorCode.INTERNAL_ERROR.newBusinessException(CRASH_MESSAGE);
						} else if (crashType.startsWith("ConnectionException")) {
							throw new ConnectionException(null, CRASH_MESSAGE);
						} else if (crashType.startsWith("ApplicationException")) {
							throw CommonErrorCode.SERVER_ERROR.newApplicationException(CRASH_MESSAGE);
						} else if (crashType.startsWith("RuntimeException")) {
							throw new RuntimeException(CRASH_MESSAGE);
						}
					}
				};
				if (workerThread) {
					ExecutorUtils.execute(runnable);
				} else {
					runnable.run();
				}
				
			}
		});
		
		return debugInfoView;
	}
}
