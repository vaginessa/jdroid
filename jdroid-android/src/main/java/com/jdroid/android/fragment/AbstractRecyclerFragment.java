package com.jdroid.android.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jdroid.android.R;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

/**
 * @param <T>
 */
public abstract class AbstractRecyclerFragment<T> extends AbstractFragment implements View.OnClickListener {

	private final static Logger LOGGER = LoggerUtils.getLogger(AbstractRecyclerFragment.class);

	private RecyclerView recyclerView;
	private RecyclerViewAdapter adapter;
	private RecyclerView.LayoutManager layoutManager;
	private View emptyView;
	private RecyclerView.AdapterDataObserver adapterDataObserver;

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

		emptyView = findView(android.R.id.empty);

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

	// use this setting to improve performance if you know that changes
	// in content do not change the layout size of the RecyclerView
	protected Boolean hasRecyclerViewFixedSize() {
		return true;
	}

	protected RecyclerView.LayoutManager createLayoutManager() {
		return new LinearLayoutManager(getActivity());
	}

	public void setAdapter(RecyclerViewAdapter adapter) {
		this.adapter = adapter;

		adapter.setOnClickListener(this);
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
		if (emptyView != null) {
			emptyView.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
		}
	}

	@Override
	public void onClick(View view) {
		int itemPosition = recyclerView.getChildAdapterPosition(view);
		if (itemPosition != RecyclerView.NO_POSITION) {
			onItemSelected((T)adapter.getItem(itemPosition));
		} else {
			LOGGER.warn("Ignored onClick for item with no position");
		}
	}

	public void onItemSelected(T item) {
		// Do Nothing
	}
}

