package com.jdroid.android.debug;

import android.app.Activity;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.R;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;

import java.util.List;

public class DebugInfoAdapter extends BaseHolderArrayAdapter<Pair<String, Object>, DebugInfoAdapter.DebugInfoHolder> {

	public DebugInfoAdapter(Activity context, List<Pair<String, Object>> items) {
		super(context, R.layout.debug_info_item, items);
	}

	@Override
	protected void fillHolderFromItem(Pair<String, Object> item, DebugInfoHolder holder) {
		holder.name.setText(item.first + ": " + item.second.toString());
	}

	@Override
	protected DebugInfoHolder createViewHolderFromConvertView(View convertView) {
		DebugInfoHolder holder = new DebugInfoHolder();
		holder.name = findView(convertView, com.jdroid.android.R.id.name);
		return holder;
	}

	public static class DebugInfoHolder {
		protected TextView name;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}