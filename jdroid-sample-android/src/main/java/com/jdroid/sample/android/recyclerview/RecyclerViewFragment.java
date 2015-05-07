package com.jdroid.sample.android.recyclerview;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.jdroid.android.fragment.AbstractRecyclerFragment;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.sample.android.R;

import java.util.List;

public class RecyclerViewFragment extends AbstractRecyclerFragment<String> {

	private List<String> items = Lists.newArrayList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen");
	private SampleRecyclerAdapter adapter;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		adapter = new SampleRecyclerAdapter(R.layout.home_item, items);
		setAdapter(adapter);

	}

	@Override
	public void onItemSelected(String item) {
		adapter.removeItem(item);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.add:
				adapter.addItem(IdGenerator.getIntId().toString());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
