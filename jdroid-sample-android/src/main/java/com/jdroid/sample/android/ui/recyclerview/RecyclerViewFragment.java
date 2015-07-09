package com.jdroid.sample.android.ui.recyclerview;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.jdroid.android.fragment.AbstractRecyclerFragment;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.sample.android.R;
import com.jdroid.sample.android.usecase.SampleUseCase;

public class RecyclerViewFragment extends AbstractRecyclerFragment<String> {

	private SampleRecyclerAdapter adapter;

	private SampleUseCase sampleUseCase;

	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sampleUseCase = getInstance(SampleUseCase.class);
	}

	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(sampleUseCase, this, FragmentHelper.UseCaseTrigger.ONCE);
	}

	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(sampleUseCase, this);
	}

	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			@Override
			public void run() {
				adapter = new SampleRecyclerAdapter(R.layout.home_item, sampleUseCase.getItems());
				setAdapter(adapter);
				dismissLoading();
			}
		});
	}

	@Override
	public void onItemSelected(String item, View view) {
		adapter.removeItem(item);
	}

	@Override
	public Integer getMenuResourceId() {
		return R.menu.recycler_menu;
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
