package com.jdroid.android.recycler;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jdroid.android.R;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.NonBlockingLoading;

public abstract class AbstractRecyclerFragment extends AbstractFragment {

	private RecyclerView recyclerView;
	private RecyclerViewAdapter adapter;
	protected ViewGroup emptyViewContainer;
	private RecyclerView.AdapterDataObserver adapterDataObserver;
	private RecyclerView.LayoutManager layoutManager;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.recycler_vertical_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		recyclerView = findView(R.id.recyclerView);
		recyclerView.setHasFixedSize(hasRecyclerViewFixedSize());
		layoutManager = createLayoutManager();
		recyclerView.setLayoutManager(layoutManager);

		if (isDividerItemDecorationEnabled()) {
			recyclerView.addItemDecoration(createDividerItemDecoration());
		}

		emptyViewContainer = findView(R.id.emptyViewContainer);

		if (adapterDataObserver == null) {
			adapterDataObserver = new RecyclerView.AdapterDataObserver() {
				@Override
				public void onItemRangeInserted(int positionStart, int itemCount) {
					refreshEmptyView();
				}

				@Override
				public void onItemRangeRemoved(int positionStart, int itemCount) {
					refreshEmptyView();
				}
			};
		}
		if (adapter != null) {
			setAdapter(adapter);
		}
	}

	protected RecyclerView.ItemDecoration createDividerItemDecoration(){
		return new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
	}

	protected Boolean isDividerItemDecorationEnabled() {
		return false;
	}

	// use this setting to improve performance if you know that changes
	// in content do not change the layout size of the RecyclerView
	protected Boolean hasRecyclerViewFixedSize() {
		return true;
	}

	@NonNull
	protected RecyclerView.LayoutManager createLayoutManager() {
		return new LinearLayoutManager(getActivity());
	}

	public void setAdapter(RecyclerViewAdapter adapter) {
		this.adapter = adapter;

		adapter.registerAdapterDataObserver(adapterDataObserver);
		recyclerView.setAdapter(adapter);

		refreshEmptyView();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (adapter != null && adapterDataObserver != null) {
			adapter.unregisterAdapterDataObserver(adapterDataObserver);
		}
	}

	private void refreshEmptyView() {
		if (emptyViewContainer != null) {
			int emptyViewContainerVisibility;
			if (adapter.getItemCount() == 0) {
				emptyViewContainerVisibility = View.VISIBLE;
				if (emptyViewContainer.getChildCount() == 0) {
					View emptyView = createEmptyView();
					if (emptyView != null) {
						emptyViewContainer.addView(emptyView);
					} else {
						emptyViewContainerVisibility = View.GONE;
					}
				}
			} else {
				emptyViewContainerVisibility = View.GONE;
			}
			emptyViewContainer.setVisibility(emptyViewContainerVisibility);
		}
	}

	protected View createEmptyView() {
		TextView emptyTextView = (TextView)inflate(R.layout.empty_view);
		emptyTextView.setText(getNoResultsResId());
		return emptyTextView;
	}

	protected int getNoResultsResId() {
		return R.string.noResults;
	}

	public RecyclerViewAdapter getAdapter() {
		return adapter;
	}

	public RecyclerView getRecyclerView() {
		return recyclerView;
	}

	public RecyclerView.LayoutManager getLayoutManager() {
		return layoutManager;
	}

	@Override
	public FragmentLoading getDefaultLoading() {
		return new NonBlockingLoading();
	}
}

