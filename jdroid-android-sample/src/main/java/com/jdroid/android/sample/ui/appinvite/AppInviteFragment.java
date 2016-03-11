package com.jdroid.android.sample.ui.appinvite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jdroid.android.about.appinvite.AppInviteHelper;
import com.jdroid.android.about.appinvite.AppInviteStats;
import com.jdroid.android.about.appinvite.AppInviteView;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.java.utils.RandomUtils;

public class AppInviteFragment extends AbstractFragment {

	private static final int REQUEST_INVITE = RandomUtils.get16BitsInt();
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.app_invite_fragment;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		AppInviteView appInviteView = findView(R.id.appInvite);
		appInviteView.setRequestCode(REQUEST_INVITE);
		appInviteView.configure(getActivity());

		AppInviteView appInviteWithStats = findView(R.id.appInviteWithStats);
		if (AppInviteStats.displayAppInviteView(getActivity())) {
			appInviteWithStats.setRequestCode(REQUEST_INVITE);
			appInviteWithStats.configure(getActivity());
			appInviteWithStats.setVisibility(View.VISIBLE);
		} else {
			appInviteWithStats.setVisibility(View.GONE);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		AppInviteHelper.onActivityResult(REQUEST_INVITE, requestCode, resultCode, data);
	}
}
