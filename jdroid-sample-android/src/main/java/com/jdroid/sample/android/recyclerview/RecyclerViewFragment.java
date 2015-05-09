package com.jdroid.sample.android.recyclerview;

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

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
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
