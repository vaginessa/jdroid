package com.jdroid.android.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class SimpleRecyclerViewType extends RecyclerViewType<Object, EmptyViewHolder> {

	@Override
	protected Class<Object> getItemClass() {
		return Object.class;
	}

	@Override
	public RecyclerView.ViewHolder createViewHolderFromView(View view) {
		return new EmptyViewHolder(view);
	}

	@Override
	public void fillHolderFromItem(Object item, EmptyViewHolder holder) {
		// Do nothing
	}
}