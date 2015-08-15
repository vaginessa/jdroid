package com.jdroid.android.sample.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.sample.R;

import java.util.List;

public class SampleAdapter extends RecyclerViewAdapter<String, SampleAdapter.SampleHolder> {

	public SampleAdapter(List<String> items) {
		super(R.layout.home_item, items);
	}

	@Override
	protected void fillHolderFromItem(String item, SampleHolder holder) {
		holder.name.setText(item);
	}

	@Override
	protected SampleHolder createViewHolderFromView(View view) {
		SampleHolder holder = new SampleHolder(view);
		holder.name = findView(view, com.jdroid.android.R.id.name);
		return holder;
	}

	public static class SampleHolder extends RecyclerView.ViewHolder {

		protected TextView name;

		public SampleHolder(View itemView) {
			super(itemView);
		}
	}
}