package com.jdroid.sample.android.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.sample.android.R;

import java.util.List;

public class SampleRecyclerAdapter extends RecyclerViewAdapter<String, SampleRecyclerAdapter.ViewHolder> {

	public SampleRecyclerAdapter(Integer resource, List<String> items) {
		super(resource, items);
	}

	@Override
	protected ViewHolder createViewHolderFromView(View view) {
		ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.textView = (TextView)view.findViewById(R.id.name);
		return viewHolder;
	}

	@Override
	protected void fillHolderFromItem(String item, ViewHolder holder) {
		holder.textView.setText(item);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		public TextView textView;

		public ViewHolder(View itemView) {
			super(itemView);
		}
	}
}