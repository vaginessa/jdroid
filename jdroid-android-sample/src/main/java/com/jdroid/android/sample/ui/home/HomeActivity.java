package com.jdroid.android.sample.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.android.sample.shortcuts.AppShortcutsCommand;
import com.jdroid.android.uri.UriHandler;

public class HomeActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return HomeFragment.class;
	}

	@Override
	public NavDrawer createNavDrawer(AbstractFragmentActivity activity, Toolbar appBar) {
		NavDrawer navDrawer = super.createNavDrawer(activity, appBar);
		navDrawer.setIsNavDrawerTopLevelView(true);
		return navDrawer;
	}

	@Override
	public UriHandler createUriHandler() {
		return new HomeUriHandler();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			new AppShortcutsCommand().start();
		}
	}
}
