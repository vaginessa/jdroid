package com.jdroid.sample.android.ui.navdrawer;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.sample.android.R;

public class RightSampleNavDrawer extends NavDrawer {

	public RightSampleNavDrawer(AbstractFragmentActivity activity, Boolean darkTheme, Toolbar appBar) {
		super(activity, darkTheme, appBar);
	}

	@Override
	public View createContentView() {
		return getActivity().findViewById(R.id.drawer);
	}
}
