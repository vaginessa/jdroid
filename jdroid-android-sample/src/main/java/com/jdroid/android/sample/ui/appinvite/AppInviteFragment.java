package com.jdroid.android.sample.ui.appinvite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jdroid.android.about.AppInviteHelper;
import com.jdroid.android.about.AppInviteView;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.java.utils.IdGenerator;

public class AppInviteFragment extends AbstractFragment {

	private static final int REQUEST_INVITE = IdGenerator.getRandom16BitsIntId();
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.app_invite_fragment;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		AppInviteView appInviteView = findView(R.id.appInvite);
		appInviteView.setRequestCode(REQUEST_INVITE);
		appInviteView.init(getActivity());
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		AppInviteHelper.onActivityResult(REQUEST_INVITE, requestCode, resultCode, data);
	}
}
