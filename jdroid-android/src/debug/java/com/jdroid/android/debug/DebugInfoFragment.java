package com.jdroid.android.debug;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.fragment.AbstractRecyclerFragment;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.ScreenUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.StringUtils;

import java.util.List;

public class DebugInfoFragment extends AbstractRecyclerFragment<Pair<String, Object>> {

	private List<Pair<String, Object>> properties = Lists.newArrayList();

	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AppContext appContext = AbstractApplication.get().getAppContext();
		properties.add(new Pair<String, Object>("Build Type", appContext.getBuildType()));
		properties.add(new Pair<String, Object>("Analytics Enabled", appContext.isGoogleAnalyticsEnabled()));
		properties.add(new Pair<String, Object>("Analytics Tracking Id", appContext.getGoogleAnalyticsTrackingId()));
		properties.add(new Pair<String, Object>("Ads Enabled", appContext.areAdsEnabled()));
		properties.add(new Pair<String, Object>("Google Project Id", appContext.getGoogleProjectId()));
		properties.add(new Pair<String, Object>("Installation Source", appContext.getInstallationSource()));
		properties.add(new Pair<String, Object>("Crashlytics Enabled", appContext.isCrashlyticsEnabled()));

		properties.add(new Pair<String, Object>("Smallest Screen Width Dp", ScreenUtils.getSmallestScreenWidthDp()));
		properties.add(new Pair<String, Object>("Screen Density", ScreenUtils.getScreenDensity()));

		properties.add(new Pair<String, Object>("Git Branch", AbstractApplication.get().getGitContext().getBranch()));
		properties.add(new Pair<String, Object>("Git Sha", AbstractApplication.get().getGitContext().getSha()));

		properties.add(new Pair<String, Object>("Application Id", AndroidUtils.getApplicationId()));
		properties.add(new Pair<String, Object>("Version Name", AndroidUtils.getVersionName()));
		properties.add(new Pair<String, Object>("Version Code", AndroidUtils.getVersionCode()));
		properties.add(new Pair<String, Object>("API Level", AndroidUtils.getApiLevel()));
		properties.add(new Pair<String, Object>("Device Manufacturer", AndroidUtils.getDeviceManufacturer()));
		properties.add(new Pair<String, Object>("Device Model", AndroidUtils.getDeviceModel()));
		properties.add(new Pair<String, Object>("Network Operator Name", AndroidUtils.getNetworkOperatorName()));
		properties.add(new Pair<String, Object>("Sim Operator Name", AndroidUtils.getSimOperatorName()));

		properties.add(new Pair<String, Object>("Device Year Class", AbstractApplication.get().getDeviceYearClass()));

		properties.addAll(AbstractApplication.get().getDebugContext().getCustomDebugInfoProperties());
	}

	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		List<Pair<String, Object>> filteredProperties = Lists.newArrayList();
		for(Pair<String, Object> each: properties) {
			if (each.second != null && StringUtils.isNotBlank(each.second.toString())) {
				filteredProperties.add(each);
			}
		}
		setAdapter(new DebugInfoAdapter(filteredProperties));
	}
}
