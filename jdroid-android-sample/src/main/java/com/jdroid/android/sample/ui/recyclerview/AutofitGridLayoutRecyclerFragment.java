package com.jdroid.android.sample.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.jdroid.android.recycler.AutofitGridLayoutManager;
import com.jdroid.android.sample.R;
import com.jdroid.android.utils.ScreenUtils;

public class AutofitGridLayoutRecyclerFragment extends SimpleRecyclerFragment {

	@NonNull
	@Override
	protected RecyclerView.LayoutManager createLayoutManager() {
		return new AutofitGridLayoutManager(getActivity(), ScreenUtils.convertDimenToPixel(R.dimen.autoGridItemWidth));
	}

}
