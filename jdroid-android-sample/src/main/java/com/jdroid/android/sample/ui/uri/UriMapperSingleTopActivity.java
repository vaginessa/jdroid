package com.jdroid.android.sample.ui.uri;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.uri.UriHandler;

public class UriMapperSingleTopActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return CommonUriMapperFragment.class;
	}

	@Override
	public UriHandler getUriHandler() {
		return new UriMapperSingleTopUriHandler();
	}
}