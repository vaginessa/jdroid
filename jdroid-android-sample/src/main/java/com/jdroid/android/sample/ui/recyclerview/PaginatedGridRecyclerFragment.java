package com.jdroid.android.sample.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class PaginatedGridRecyclerFragment extends PaginatedRecyclerFragment {

	@NonNull
	@Override
	protected RecyclerView.LayoutManager createLayoutManager() {
		return new GridLayoutManager(getActivity(), 3);
	}

}
