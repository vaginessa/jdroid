package com.jdroid.sample.android.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.adapter.BaseHolderArrayAdapter;

import java.util.List;

public class SampleAdapter extends BaseHolderArrayAdapter<String, SampleAdapter.SampleHolder> {

	public SampleAdapter(Activity context, List<String> items) {
		super(context, com.jdroid.android.R.layout.contextual_list_item, items);
	}

	@Override
	protected void fillHolderFromItem(String item, SampleHolder holder) {
		holder.name.setText(item);
	}

	@Override
	protected SampleHolder createViewHolderFromConvertView(View convertView) {
		SampleHolder holder = new SampleHolder();
		holder.name = findView(convertView, com.jdroid.android.R.id.name);
		return holder;
	}

	public static class SampleHolder {
		protected TextView name;
	}
}