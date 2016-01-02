package com.jdroid.android.sample.ui.loading;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.jdroid.android.fragment.FragmentHelper.UseCaseTrigger;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.SwipeRecyclerFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.ui.adapter.SampleRecyclerViewType;
import com.jdroid.android.sample.usecase.SampleUseCase;
import com.jdroid.java.utils.IdGenerator;

public class SwipeRefreshLoadingFragment extends SwipeRecyclerFragment {
	
	private SampleUseCase sampleUseCase;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sampleUseCase = getInstance(SampleUseCase.class);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(sampleUseCase, this, UseCaseTrigger.ONCE);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(sampleUseCase, this);
	}
	
	@Override
	public void onRefresh() {
		if (!sampleUseCase.isInProgress()) {
			executeUseCase(sampleUseCase);
		}
	}

	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			@Override
			public void run() {
				setAdapter(new RecyclerViewAdapter(new SampleRecyclerViewType() {

					@Override
					public void onItemSelected(String item, View view) {
						getAdapter().removeItem(item);
					}

					@Override
					public AbstractRecyclerFragment getAbstractRecyclerFragment() {
						return SwipeRefreshLoadingFragment.this;
					}
				}, sampleUseCase.getItems()));
				dismissLoading();
			}
		});
	}

	@Override
	public Integer getMenuResourceId() {
		return R.menu.recycler_menu;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.add:
				getAdapter().addItem(IdGenerator.getIntId().toString());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
