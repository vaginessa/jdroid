package com.jdroid.sample.android.ui.navdrawer;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.sample.android.R;

public class LeftSampleNavDrawer extends NavDrawer {

	public LeftSampleNavDrawer(AbstractFragmentActivity activity, Toolbar appBar) {
		super(activity, appBar);
	}

	@Override
	public View createContentView() {
		return getActivity().findViewById(R.id.drawer);
	}
}
