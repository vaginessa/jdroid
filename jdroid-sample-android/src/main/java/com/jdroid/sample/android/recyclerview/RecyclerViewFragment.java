package com.jdroid.sample.android.recyclerview;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.fragment.AbstractRecyclerFragment;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.sample.android.R;

import java.util.List;

public class RecyclerViewFragment extends AbstractRecyclerFragment<String> {

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		List<String> items = Lists.newArrayList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen");
		SampleRecyclerAdapter adapter = new SampleRecyclerAdapter(R.layout.home_item, items);
		setAdapter(adapter);

	}

	@Override
	public void onItemSelected(String item) {
		ToastUtils.showInfoToast(item);
	}
}
