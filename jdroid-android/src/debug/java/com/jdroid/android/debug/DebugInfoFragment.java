package com.jdroid.android.debug;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.DeviceUtils;
import com.jdroid.android.utils.ScreenUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.StringUtils;

import java.util.List;

public class DebugInfoFragment extends AbstractRecyclerFragment {

	private List<Pair<String, Object>> properties = Lists.newArrayList();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AppContext appContext = AbstractApplication.get().getAppContext();

		properties.add(new Pair<String, Object>("Build Type", appContext.getBuildType()));
		properties.add(new Pair<String, Object>("Build Time", appContext.getBuildTime()));

		properties.add(new Pair<String, Object>("Analytics Enabled", appContext.isGoogleAnalyticsEnabled()));
		properties.add(new Pair<String, Object>("Analytics Tracking Id", appContext.getGoogleAnalyticsTrackingId()));
		properties.add(new Pair<String, Object>("Installation Source", appContext.getInstallationSource()));

		properties.add(new Pair<String, Object>("Smallest Screen Width Dp", ScreenUtils.getSmallestScreenWidthDp()));
		properties.add(new Pair<String, Object>("Screen Density", ScreenUtils.getScreenDensity()));

		properties.add(new Pair<String, Object>("Git Branch", AbstractApplication.get().getGitContext().getBranch()));
		properties.add(new Pair<String, Object>("Git Sha", AbstractApplication.get().getGitContext().getSha()));

		properties.add(new Pair<String, Object>("Application Id", AndroidUtils.getApplicationId()));
		properties.add(new Pair<String, Object>("Version Name", AndroidUtils.getVersionName()));
		properties.add(new Pair<String, Object>("Version Code", AndroidUtils.getVersionCode()));
		properties.add(new Pair<String, Object>("API Level", AndroidUtils.getApiLevel()));

		properties.add(new Pair<String, Object>("Device Manufacturer", DeviceUtils.getDeviceManufacturer()));
		properties.add(new Pair<String, Object>("Device Model", DeviceUtils.getDeviceModel()));
		properties.add(new Pair<String, Object>("Device Year Class", DeviceUtils.getDeviceYearClass()));

		properties.add(new Pair<String, Object>("Network Operator Name", AndroidUtils.getNetworkOperatorName()));
		properties.add(new Pair<String, Object>("Sim Operator Name", AndroidUtils.getSimOperatorName()));

		properties.addAll(AbstractApplication.get().getDebugContext().getCustomDebugInfoProperties());
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		List<Pair<String, Object>> filteredProperties = Lists.newArrayList();
		for(Pair<String, Object> each: properties) {
			if (each.second != null && StringUtils.isNotBlank(each.second.toString())) {
				filteredProperties.add(each);
			}
		}
		setAdapter(new RecyclerViewAdapter(new DebugInfoRecyclerViewType(), filteredProperties));
	}

	public class DebugInfoRecyclerViewType extends RecyclerViewType<Pair, DebugInfoHolder> {

		@Override
		protected Class<Pair> getItemClass() {
			return Pair.class;
		}

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.debug_info_item;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			DebugInfoHolder holder = new DebugInfoHolder(view);
			holder.name = findView(view, com.jdroid.android.R.id.name);
			return holder;
		}

		@Override
		public void fillHolderFromItem(Pair item, DebugInfoHolder holder) {
			holder.name.setText(item.first + ": " + item.second.toString());
		}

		@Override
		public AbstractRecyclerFragment getAbstractRecyclerFragment() {
			return DebugInfoFragment.this;
		}
	}

	public static class DebugInfoHolder extends RecyclerView.ViewHolder {

		protected TextView name;

		public DebugInfoHolder(View itemView) {
			super(itemView);
		}
	}
}
