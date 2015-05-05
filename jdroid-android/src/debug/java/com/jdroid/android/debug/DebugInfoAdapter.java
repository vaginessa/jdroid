package com.jdroid.android.debug;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.R;
import com.jdroid.android.recycler.RecyclerViewAdapter;

import java.util.List;

public class DebugInfoAdapter extends RecyclerViewAdapter<Pair<String, Object>, DebugInfoAdapter.DebugInfoHolder> {

	public DebugInfoAdapter(List<Pair<String, Object>> items) {
		super(R.layout.debug_info_item, items);
	}

	@Override
	protected void fillHolderFromItem(Pair<String, Object> item, DebugInfoHolder holder) {
		holder.name.setText(item.first + ": " + item.second.toString());
	}

	@Override
	protected DebugInfoHolder createViewHolderFromView(View view) {
		DebugInfoHolder holder = new DebugInfoHolder(view);
		holder.name = findView(view, com.jdroid.android.R.id.name);
		return holder;
	}

	public static class DebugInfoHolder extends RecyclerView.ViewHolder {

		protected TextView name;

		public DebugInfoHolder(View itemView) {
			super(itemView);
		}
	}
}