package com.jdroid.android.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.recycler_vertical_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		recyclerView = findView(R.id.recyclerView);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		recyclerView.setHasFixedSize(true);

		// use a linear layout manager
		layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);

		if (adapter != null) {
			setAdapter(adapter);
		}
	}

	public void setAdapter(RecyclerViewAdapter adapter) {
		this.adapter = adapter;
		adapter.setOnClickListener(this);
		recyclerView.setAdapter(adapter);
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

